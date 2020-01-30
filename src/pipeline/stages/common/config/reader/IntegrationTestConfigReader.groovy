package pipeline.stages.common.config.reader

import org.w3c.dom.Element
import pipeline.stages.common.config.reader.common.CommandsReader
import pipeline.stages.integrationtest.config.IntegrationTestsStageConfig

import static pipeline.stages.common.utils.XmlDomUtils.getOnlyElement
import static pipeline.stages.common.utils.XmlDomUtils.getOnlyElementTextContent

final class IntegrationTestConfigReader {

    private IntegrationTestConfigReader(){

    }

    static IntegrationTestsStageConfig parseIntegrationTestsConfig(Element stage) {
        def result = new IntegrationTestsStageConfig()
        result.label = getOnlyElementTextContent(stage, "label")

        def commandsNode = getOnlyElement(stage, "commands")

        if (commandsNode != null) {
            def parsedCommands = CommandsReader.parseCommands(commandsNode)
            result.commands = parsedCommands
        }

        return result
    }
}
