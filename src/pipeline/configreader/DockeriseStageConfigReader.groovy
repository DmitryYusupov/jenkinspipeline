package pipeline.configreader

import org.w3c.dom.Element
import pipeline.stages.dockerise.config.AccessConfig
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig

import static utils.XmlDomUtils.getOnlyElement
import static utils.XmlDomUtils.getOnlyElementTextContent

final class DockeriseStageConfigReader {
    private DockeriseStageConfigReader() {

    }

    static DockeriseStageConfig parseDockeriseStageConfig(Element stage) {
        def result = new DockeriseStageConfig()
        result.label = getOnlyElementTextContent(stage, "label")
        result.numberOfImagesToStore = Integer.parseInt(getOnlyElementTextContent(stage, "numberOfImagesToStore"));

        def configs = getOnlyElement(stage, "configs")

        if (configs != null) {
            for (int i = 0; i < configs.childNodes.length; i++) {
                def config = configs.childNodes.item(i)

                def configName = config.getNodeName()
                switch (configName) {
                    case "buildImageConfig":
                        result.buildImageConfig = parseBuildImageConfig((Element) config)
                        break
                    case "accessConfig":
                        result.accessConfig = parseAccessConfig((Element) config)
                        break
                }
            }
        }

        return result
    }

    private static BuildImageConfig parseBuildImageConfig(Element element) {
        def result = new BuildImageConfig()
        result.dockerFileDir = getOnlyElementTextContent(element, "dockerFileDir")
        result.imageName = getOnlyElementTextContent(element, "imageName")
        result.tagPrefix = getOnlyElementTextContent(element, "tagPrefix")

        return result
    }

    private static AccessConfig parseAccessConfig(Element element) {
        def result = new AccessConfig()
        result.login = getOnlyElementTextContent(element, "login")
        result.dokcerRegistryUrl = getOnlyElementTextContent(element, "dokcerRegistryUrl")

        return result
    }


}
