
import pipeline.stages.checkout.config.CheckoutStageConfig

def doCheckout(CheckoutStageConfig checkoutConfig) {
    println("============================BEGIN $checkoutConfig.label ============================")
    stage(checkoutConfig.label) {
        checkout scm: [$class           : 'GitSCM',
                       branches         : [[name: checkoutConfig.gitConfig.branch]],
                       userRemoteConfigs: [[url: checkoutConfig.gitConfig.url]]
        ]
    }
    println("============================END $checkoutConfig.label ============================")
}