import pipeline.stage.checkout.config.CheckoutConfigStage

def doCheckout(CheckoutConfigStage checkoutConfig) {
    stage(checkoutConfig.stageName) {
        checkout scm: [$class                           : 'GitSCM',
                       branches                         : [[name: checkoutConfig.gitConfig.brunch]],
                       userRemoteConfigs                : [[url: checkoutConfig.gitConfig.url]]
        ]
    }
}