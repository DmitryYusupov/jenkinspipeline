package pipeline.stages.config

import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.checkout.config.CheckoutStageConfig
import pipeline.stages.integrationtest.config.IntegrationTestsStageConfig

class PipelineConfig {
    CheckoutStageConfig checkoutStageConfig
    BuildStageConfig buildStageConfig
    IntegrationTestsStageConfig integrationTestsConfig
}
