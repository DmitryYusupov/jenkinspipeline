import pipeline.stages.common.stage.PipelineContext

def handleException(Exception e, PipelineContext context) {
    println("Pipeline Exception " + e.toString())
    stageDockerise.revertStageChanges(e, context)
}