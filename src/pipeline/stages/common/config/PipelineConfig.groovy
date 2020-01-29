package pipeline.stages.common.config

import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.checkout.config.CheckoutStageConfig

class PipelineConfig {
    CheckoutStageConfig checkoutStageConfig
    BuildStageConfig buildStageConfig
}
