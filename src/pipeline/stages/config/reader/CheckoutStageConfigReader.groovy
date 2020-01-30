package pipeline.stages.config.reader

import org.w3c.dom.Element
import pipeline.stages.checkout.config.CheckoutStageConfig
import pipeline.stages.checkout.config.GitConfig

import static utils.XmlDomUtils.*

class CheckoutStageConfigReader {
    private CheckoutStageConfigReader(){

    }

     static CheckoutStageConfig parseCheckoutConfig(Element stage) {
        def result = new CheckoutStageConfig()
        result.label = getOnlyElementTextContent(stage, "label")

        def configs = getOnlyElement(stage, "configs")
        if (configs != null) {

            def gitConfig = getOnlyElement(configs, "gitconfig")
            if (gitConfig != null) {
                result.gitConfig = parseGitConfig(gitConfig)
            }
        }

        return result
    }


    private static GitConfig parseGitConfig(Element gitConfigElem) {
        GitConfig result = new GitConfig()
        result.branch = getOnlyElementTextContent(gitConfigElem, "branch")
        result.tag = getOnlyElementTextContent(gitConfigElem, "tag")
        result.url = getOnlyElementTextContent(gitConfigElem, "url")

        return result
    }

}
