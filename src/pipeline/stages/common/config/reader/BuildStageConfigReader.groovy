package pipeline.stages.common.config.reader

import org.w3c.dom.Element
import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.common.config.reader.common.CommandsReader

import static pipeline.stages.common.utils.XmlDomUtils.getOnlyElement
import static pipeline.stages.common.utils.XmlDomUtils.getOnlyElementTextContent

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
