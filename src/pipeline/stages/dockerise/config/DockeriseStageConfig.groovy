package pipeline.stages.dockerise.config

import pipeline.stages.common.stage.BaseStageConfig

class DockeriseStageConfig extends BaseStageConfig {
    int numberOfImagesToStore;
    BuildImageConfig buildImageConfig
    PushImageConfig pushImageConfig
}
