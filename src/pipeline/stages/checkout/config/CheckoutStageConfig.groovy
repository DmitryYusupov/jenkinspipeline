package pipeline.stages.checkout.config

import pipeline.stages.common.stage.BaseStageConfig

//@Grab(group = 'org.apache.commons', module = 'commons-lang3', version = '3.6')
//import org.apache.commons.lang3.StringUtils

class CheckoutStageConfig extends BaseStageConfig{
    GitConfig gitConfig
}
