package pipeline.stages.common.stage

import pipeline.config.GlobalPipelineConfigs
import pipeline.stages.dockerise.context.DockeriseStageContext

class PipelineContext {
    Exception exception
    GlobalPipelineConfigs globalPipelineConfigs
    DockeriseStageContext dockeriseStageContext
}
