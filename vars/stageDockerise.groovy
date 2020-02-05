import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
import pipeline.stages.dockerise.exception.DockeriseStageException

def doContainerisation(DockeriseStageConfig dockeriseConfig, PipelineContext pipelineContext) {
    println("============================BEGIN $dockeriseConfig.label ============================")
    stage(dockeriseConfig.label) {
        def image = buildImage(dockeriseConfig.buildImageConfig)
        println("Image was build " + image)
    }
    println("============================END $dockeriseConfig.label ============================")
}

def buildImage(BuildImageConfig buildImageConfig) {

    def tagName = buildImageConfig.tagPrefix + "_" + env.BUILD_ID

    def dockerFileDir = buildImageConfig.dockerFileDir
    if (dockerFileDir == null || dockerFileDir.isEmpty()) {
        def image = docker.build("$buildImageConfig.imageName:$tagName")
        return image
    } else {
        def image = docker.build("$buildImageConfig.imageName:$tagName", dockerFileDir)
        return image
    }
}

def handleException(DockeriseStageException e, PipelineContext pipelineContext) {
    println("Handle dockerize exception")
}