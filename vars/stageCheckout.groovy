
import pipeline.stages.checkout.config.CheckoutStageConfig

def doCheckout(CheckoutStageConfig checkoutConfig) {
    println("============================BEGIN $checkoutConfig.stageName ============================")
    stage(checkoutConfig.stageName) {
        checkout scm: [$class           : 'GitSCM',
                       branches         : [[name: checkoutConfig.gitConfig.brunch]],
                       userRemoteConfigs: [[url: checkoutConfig.gitConfig.url]]
        ]
    }
    println("============================END $checkoutConfig.stageName ============================")
}