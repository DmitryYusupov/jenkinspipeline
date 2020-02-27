package pipeline.configreader

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import pipeline.config.GlobalPipelineConfigs
import pipeline.config.PipelineConfig
import pipeline.stages.Stage

import java.nio.file.FileSystems
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern

import static utils.XmlDomUtils.getDocument
import static utils.XmlDomUtils.getOnlyElementFromDocument
import static utils.XmlDomUtils.getOnlyElementTextContent

class ConfigReader {

    public static void main(String[] args) {
        //def rr = parsePipelineConfig("C:\\Users\\Dmitry_Yusupov\\Desktop\\Jenkins_pipeline\\jenkinspipeline\\projects\\Shop\\pipeline.xml");
       // def ff = FileSystems.getDefault()
        /*println("assa")*/
      //  println()

/*        def regExp = "(\\s+)(\\w+)(\\s+)(\\w+)"
        Pattern pattern = Pattern.compile(regExp)
        String s = "                                                    env1_27                    270f8030ea54        45 hours ago        291MB"
        Matcher matcher = pattern.matcher(s)


        if (matcher.find() && matcher.groupCount() == 4) {
            for(int i=1;i<=matcher.groupCount();i++){
                println("'" + matcher.group(i) +"'")
            }
        }*/

     /*   def ss = new ArrayList<>();
        ss.add({s -> println(s)});
        ss.forEach{
            c->c("AAAAAA")
        }*/
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
