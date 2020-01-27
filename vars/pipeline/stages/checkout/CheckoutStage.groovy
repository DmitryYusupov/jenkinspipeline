package pipeline.stages.checkout

def doCheckout(CheckoutStageConfig checkoutConfig) {
    stage(checkoutConfig.stageName) {
        checkout scm: [$class           : 'GitSCM',
                       branches         : [[name: checkoutConfig.gitConfig.brunch]],
                       userRemoteConfigs: [[url: checkoutConfig.gitConfig.url]]
        ]
    }
}