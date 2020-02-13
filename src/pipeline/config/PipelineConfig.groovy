package pipeline.config

import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.checkout.config.CheckoutStageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
import pipeline.stages.integrationtest.config.IntegrationTestsStageConfig

class PipelineConfig {
    GlobalPipelineConfigs globalPipelineConfigs;
    CheckoutStageConfig checkoutStageConfig
    BuildStageConfig buildStageConfig
    IntegrationTestsStageConfig integrationTestsStageConfig
    DockeriseStageConfig dockeriseStageConfig
}
