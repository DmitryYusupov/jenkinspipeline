package pipeline.configreader

import org.w3c.dom.Element
import pipeline.configreader.common.CommandsReader
import pipeline.stages.build.config.BuildStageConfig

import static utils.XmlDomUtils.getOnlyElement
import static utils.XmlDomUtils.getOnlyElementTextContent

final class BuildStageConfigReader {
    private BuildStageConfigReader() {

    }

    static BuildStageConfig parseBuildConfig(Element stage) {
        def result = new BuildStageConfig()
        result.label = getOnlyElementTextContent(stage, "label")

        def commandsNode = getOnlyElement(stage, "commands")

        if (commandsNode != null) {
            def parsedCommands = CommandsReader.parseCommands(commandsNode)
            result.commands = parsedCommands
        }

        return result
    }

}
