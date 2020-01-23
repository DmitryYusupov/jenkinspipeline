package common.stage

import common.configs.pipeline.stage.checkout.*

def doCheckout(CheckoutConfigStage checkoutConfig) {
    stage(checkoutConfig.stageName) {
        checkout scm: [$class                           : 'GitSCM',
                       branches                         : [[name: checkoutConfig.gitConfig.brunch]],
                       userRemoteConfigs                : [[url: checkoutConfig.gitConfig.url]]
        ]
    }
}