
import pipeline.stages.checkout.config.CheckoutStageConfig

def doCheckout(CheckoutStageConfig checkoutConfig) {
    println("============================BEGIN $checkoutConfig.label ============================")
    stage(checkoutConfig.stageName) {
        checkout scm: [$class           : 'GitSCM',
                       branches         : [[name: checkoutConfig.gitConfig.brunch]],
                       userRemoteConfigs: [[url: checkoutConfig.gitConfig.url]]
        ]
    }
    println("============================END $checkoutConfig.label ============================")
}