import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.exception.DockeriseStageException

def handleException(Exception e, PipelineContext context) {
    println("Pipeline Exception " + e.toString())

    if (e instanceof DockeriseStageException) {
        stageDockerise.revertStageChanges(e, context)
    }
}