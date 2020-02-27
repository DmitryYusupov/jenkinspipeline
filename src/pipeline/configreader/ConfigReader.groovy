package pipeline.configreader

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import pipeline.config.GlobalPipelineConfigs
import pipeline.config.PipelineConfig
import pipeline.stages.Stage
import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.context.BuildImageStageContext
import pipeline.stages.dockerise.context.DockeriseStageContext
import pipeline.stages.dockerise.exception.DockerBuildImageException
import pipeline.stages.dockerise.exception.DockerDeleteOldImagesException
import pipeline.stages.dockerise.exception.DockerImagePushException

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

        PipelineContext pipelineContext = new PipelineContext();
        pipelineContext.exception = new DockerDeleteOldImagesException("SS WW")
        pipelineContext.dockeriseStageContext = new DockeriseStageContext();
        pipelineContext.dockeriseStageContext.buildStageContext = new BuildImageStageContext();
        handleException(pipelineContext)
    }


    static void handleException(PipelineContext pipelineContext) {

        def exception = pipelineContext.exception

        def revertActions = new ArrayList<>()
        revertActions.add({ ctx ->
            println("Error on dockerise stage")
            ctx.exception.printStackTrace();
        })

        if (exception instanceof DockerBuildImageException) {
            //print error
            revertActions.add({ ctx -> println("Error while try to build image!") })
        } else if (exception instanceof DockerImagePushException) {
            //print error
            revertActions.add({ ctx -> println("Error while try to push image to repo!") })
            //print delete created image
            revertActions.add({ ctx ->
                if (ctx.dockeriseStageContext.buildStageContext != null) {
                   // revertBuildImageStageChanges(ctx.dockeriseStageContext.buildStageContext)
                }
            })
        } else if (exception instanceof DockerDeleteOldImagesException) {
            println("33333333333333333333")
            //print error
            revertActions.add({ ctx -> println("Error while try to delete old images from local repo!") })
            println("44444444444444444")
            //print delete created image
            revertActions.add({ ctx ->
                if (ctx.dockeriseStageContext.buildStageContext != null) {
                    println "REVERT"
                   // revertBuildImageStageChanges(ctx.dockeriseStageContext.buildStageContext)
                }
            })
        }
        println("SSSSSSSSSSSSSSSSSSSSSSSSS")
        revertActions.forEach { a -> a(pipelineContext) }
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
