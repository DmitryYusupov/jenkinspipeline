package pipeline.stages.common.config.reader


import org.w3c.dom.Element
import org.w3c.dom.NodeList
import pipeline.stages.common.config.PipelineConfig
import pipeline.stages.common.config.Stage

import static pipeline.stages.common.utils.XmlDomUtils.*

class ConfigReader {

    static PipelineConfig parsePipelineConfig(String xmlFilePath) {
        def result = new PipelineConfig()

        File file = getFile(xmlFilePath)
        def doc = getDocument(file)

        def root = getOnlyElementFromDocument(doc, "pipeline")
        NodeList stages = root.getElementsByTagName("stages")

        for (int i = 0; i < stages.getLength(); i++) {
            def stage = stages.item(i);
            String stageNameStr = ((Element) stage).getAttribute("name")

            switch (Stage.valueOf(stageNameStr)) {

                case Stage.CHECKOUT:
                    result.setCheckoutStageConfig(CheckoutStageConfigReader.parseCheckoutConfig((Element) stage))
                    break
            }
        }

        return result
    }


    private static File getFile(String xmlFilePath) {
        File file = new File(xmlFilePath);

        if (!file.isFile()) {
            throw new RuntimeException("No such file '$xmlFilePath'")
        }

        return file
    }

}
