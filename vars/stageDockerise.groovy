import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
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

def buildImage(BuildImageConfig buildImageConfig, PipelineContext pipelineContext) {

    def curStageContext = new DockeriseStageContext()
    pipelineContext.dockeriseStageContext = curStageContext

    def tagName = buildImageConfig.tagPrefix + "_" + env.BUILD_ID
    curStageContext.tag = tagName
    curStageContext.image = buildImageConfig.imageName

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
        println("----BEGIN ErrorHandling <DockeriseStage> ----")
        try {
            def dockerContext = pipelineContext.dockeriseStageContext;
            def imageToDelete = dockerContext.image + ":" + dockerContext.tag
            println("Try delete image '$imageToDelete'")

            def deleteCreateImageCommand = "docker rmi $dockerContext.image:$dockerContext.tag"

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
}