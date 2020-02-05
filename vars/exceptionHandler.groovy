import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.exception.DockeriseStageException

def handleException(Exception e, PipelineContext context) {
    if (e instanceof DockeriseStageException) {
        stageDockerise.handleException(e, context)
    }

}