package pipeline.configreader

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import pipeline.config.GlobalPipelineConfigs
import pipeline.config.PipelineConfig
import pipeline.stages.Stage
import utils.CollectionUtils
import utils.os.process.ProcessUtils

import java.util.concurrent.Executors

import static utils.XmlDomUtils.*

class ConfigReader {

    public static void main(String[] args) {
        //def rr = parsePipelineConfig("C:\\Users\\Dmitry_Yusupov\\Desktop\\Jenkins_pipeline\\jenkinspipeline\\projects\\Shop\\pipeline.xml");
       // def ff = FileSystems.getDefault()
        /*println("assa")*/
      //  println()

        CollectionUtils.isEmpty(null)
        def tt = ProcessUtils.runProcessAndWaitForOutput("docker images --filter before=usikovich/my-image:env1_127")
        println(tt.errorOutput)

    }


    static PipelineConfig parsePipelineConfig(String xmlFilePath) {
        def result = new PipelineConfig()

        File file = getFile(xmlFilePath)
        def doc = getDocument(file)

        def root = getOnlyElementFromDocument(doc, "pipeline")
        parseAndAppendPipelineGlobalConfigs(result, root)
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

                    case Stage.INTEGRATION_TESTS:
                        result.setIntegrationTestsStageConfig(IntegrationTestConfigReader.parseIntegrationTestsConfig(stage))
                        break

                    case Stage.DOCKERISE:
                        result.setDockeriseStageConfig(DockeriseStageConfigReader.parseDockeriseStageConfig(stage))
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

    private static void parseAndAppendPipelineGlobalConfigs(PipelineConfig pipelineConfig, Element pipelineRoot) {
        def globalPipelineConfigs = new GlobalPipelineConfigs();
        globalPipelineConfigs.release = Boolean.valueOf(getOnlyElementTextContent(pipelineRoot, "release"))
        pipelineConfig.globalPipelineConfigs = globalPipelineConfigs;
    }

}
