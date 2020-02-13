import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
import pipeline.stages.dockerise.context.BuildImageStageContext
import pipeline.stages.dockerise.context.DockeriseStageContext
import utils.os.Os
import utils.os.OsUtils

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

        def deleteCreateImageCommand = "docker rmi $buildImageStageContext.image:$buildImageStageContext.tag"

        def currentOs = OsUtils.getOS()
        switch (currentOs) {
            case Os.WINDOWS:
                bat "$deleteCreateImageCommand"
                break

            case Os.UNIX:
                sh "$deleteCreateImageCommand"
                break

            default:
                throw new RuntimeException("No utility detected to execute gradle command '$deleteCreateImageCommand'")
        }
    } catch (Exception e) {
        println("Error while revert changes !! " + e.getMessage())
    }
    println("----END ErrorHandling <DockeriseStage> ----")
}

private deleteImagesIfNumberOfStoredImagesHasExpired(int maxImagesToStore, String imageName, String imageTag){
    String s   = "docker images --filter \"before=my-image:env1_27\" | grep 'my-image' | grep 'env1_'";

 //   String s   = "docker images";

    def gitCommit = bat(returnStdout: true, script: s).trim()
    println(gitCommit)
}