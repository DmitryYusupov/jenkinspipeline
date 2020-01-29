package pipeline.stages.common.config.reader


import org.w3c.dom.Element
import org.w3c.dom.NodeList
import pipeline.stages.common.config.PipelineConfig
import pipeline.stages.common.config.Stage

import java.util.function.Supplier

import static pipeline.stages.common.utils.XmlDomUtils.*

class ConfigReader {

    public static void main(String[] args) {
        def rr = parsePipelineConfig("C:\\Users\\Dmitry_Yusupov\\Desktop\\Jenkins_pipeline\\jenkinspipeline\\projects\\Shop\\pipeline.xml");

    }

    static PipelineConfig parsePipelineConfig(String xmlFilePath) {
        def result = new PipelineConfig()

        File file = getFile(xmlFilePath)
        def doc = getDocument(file)

        def root = getOnlyElementFromDocument(doc, "pipeline")
        NodeList stagesElement = root.getElementsByTagName("stages")
        def stages = ((Element) stagesElement.item(0)).getElementsByTagName("stage")
        for (int i = 0; i < stages.getLength(); i++) {
            def stage = (Element) stages.item(i)
            String stageNameStr = stage.getAttribute("name")

            Stage stageEnum = Stage.fromString(stageNameStr)

            if (stageEnum != null) {
                switch (stageEnum) {
                    case Stage.CHECKOUT:
                        result.setCheckoutStageConfig(CheckoutStageConfigReader.parseCheckoutConfig(stage))
                        break

                    case Stage.BUILD:
                        result.setBuildStageConfig(BuildStageConfigReader.parseBuildConfig(stage))
                        break

                }
            } else {
                throw new RuntimeException("No such stage by name '$stageNameStr'")
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
