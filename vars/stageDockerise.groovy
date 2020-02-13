import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.classes.DockerImage
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
import pipeline.stages.dockerise.context.BuildImageStageContext
import pipeline.stages.dockerise.context.DockeriseStageContext
import utils.os.Os
import utils.os.OsUtils

import java.util.regex.Matcher
import java.util.regex.Pattern

def doContainerisation(DockeriseStageConfig dockeriseConfig, PipelineContext pipelineContext) {
    println("============================BEGIN $dockeriseConfig.label ============================")
    stage(dockeriseConfig.label) {
        def image = buildImage(dockeriseConfig.buildImageConfig, pipelineContext)
        println("Image was build " + image)
    }
    println("============================END $dockeriseConfig.label ============================")
}

private def buildImage(BuildImageConfig buildImageConfig, PipelineContext pipelineContext) {

    def curStageContext = new DockeriseStageContext()
    def buildImageStageContext = new BuildImageStageContext()
    curStageContext.buildStageContext = buildImageStageContext

    curStageContext.buildStageContext
    pipelineContext.dockeriseStageContext = curStageContext

    def tagName = buildImageConfig.tagPrefix + "_" + env.BUILD_ID
    buildImageStageContext.tag = tagName
    buildImageStageContext.image = buildImageConfig.imageName

    def dockerFileDir = buildImageConfig.dockerFileDir
    if (dockerFileDir == null || dockerFileDir.isEmpty()) {
        def image = docker.build("$buildImageConfig.imageName:$tagName")
        return image
    } else {
        def image = docker.build("$buildImageConfig.imageName:$tagName", dockerFileDir)
        return image
    }
}

def revertStageChanges(Exception exception, PipelineContext pipelineContext) {
    if (pipelineContext.dockeriseStageContext != null) {
        if (pipelineContext.dockeriseStageContext.buildStageContext != null) {
            revertBuildImageStageChanges(exception, pipelineContext.dockeriseStageContext.buildStageContext)
        }
    }
}

private def revertBuildImageStageChanges(Exception exception, BuildImageStageContext buildImageStageContext) {
    println("----BEGIN ErrorHandling <DockeriseStage> ----")
    try {
        def imageToDelete = buildImageStageContext.image + ":" + buildImageStageContext.tag
        println("Try delete image '$imageToDelete'")
        osUtils.runCommand("docker rmi $buildImageStageContext.image:$buildImageStageContext.tag")
    } catch (Exception e) {
        println("Error while revert changes !! " + e.getMessage())
    }
    println("----END ErrorHandling <DockeriseStage> ----")
}

private deleteImagesIfNumberOfStoredImagesHasExpired(int maxImagesToStore, String imageName, String imageTag, String imageTagPrefix) {
    def command = getCommandToGetDockerImages(imageName, imageTag, imageTagPrefix)
    def output = osUtils.runCommandReturningOutput(command)
    def images = parseDockerImagesDataFromOutputString(output, imageName)

    for (DockerImage img : images) {
        println(img.toString())
    }
}

private String getCommandToGetDockerImages(String imageName, String imageTag, String imageTagPrefix) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            def command = "docker images --filter before=$imageName:$imageTag | find \"" + imageName + "\"";
            if (imageTagPrefix != null && !imageTagPrefix.isEmpty()) {
                command = command + "| find \"" + imageTagPrefix + "\"";
            }
            return command

        case Os.UNIX:
            def command = "docker images --filter before=$imageName:$imageTag | grep \"" + imageName + "\"";
            if (imageTagPrefix != null && !imageTagPrefix.isEmpty()) {
                command = command + "| grep \"" + imageTagPrefix + "\"";
            }
            return command

        default:
            throw new RuntimeException("Impossible to get dockerImages command. Unknown OS!")
    }
}



private List<DockerImage> parseDockerImagesDataFromOutputString(String outputStr, String imageName) {
    List<DockerImage> result = new ArrayList<>()

    def regExp = "(\\S+)(\\w+)(\\S+)(\\w+)(\\S+)(.+)"
    Pattern pattern = Pattern.compile(regExp)

    def splited = outputStr.split("\n")
    for (int i = 0; i < splited.length; i++) {
        def imageInfoStr = splited[i].trim();
        if (imageInfoStr.startsWith(imageName)) {

            def dockerImage = new DockerImage()
            dockerImage.name = imageName
            imageInfoStr = imageInfoStr.replaceFirst(imageName, "")
            println("'" + imageInfoStr + "'")

            Matcher matcher = pattern.matcher(imageInfoStr)

            if (matcher.find() && matcher.groupCount() == 6) {
                dockerImage.tag = matcher.group(2)
                dockerImage.id = matcher.group(4)
            }

            result.add(dockerImage)
        }
    }

    return result
}



