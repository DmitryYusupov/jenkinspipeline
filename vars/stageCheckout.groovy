
import pipeline.stages.checkout.config.CheckoutStageConfig
import  pipeline.stages.common.stage.PipelineContext

def doCheckout(CheckoutStageConfig checkoutConfig, PipelineContext pipelineContext) {
    println("============================BEGIN $checkoutConfig.label ============================")
    stage(checkoutConfig.label) {
        checkout scm: [$class           : 'GitSCM',
                       branches         : [[name: checkoutConfig.gitConfig.branch]],
                       userRemoteConfigs: [[url: checkoutConfig.gitConfig.url]]
        ]
    }
    println("============================END $checkoutConfig.label ============================")
}