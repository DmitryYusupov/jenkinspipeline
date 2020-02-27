import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.exception.DockeriseStageException

def handleException(PipelineContext context) {

    Exception e = context.exception
    println("Pipeline Exception " + e.toString())
    e.printStackTrace();

    if (e instanceof DockeriseStageException) {
        stageDockerise.handleException(context)
    }
}