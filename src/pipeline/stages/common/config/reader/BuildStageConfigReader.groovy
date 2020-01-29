package pipeline.stages.common.config.reader

import org.w3c.dom.Element
import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.commands.GradleCommand
import pipeline.stages.common.commands.MavenCommand

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
            result.commands = new ArrayList<>()
            def commands = commandsNode.getElementsByTagName("command")

            for (int i = 0; i < commands.length; i++) {
                def command = (Element) commands.item(i);
                result.commands.add(parseCommand(command))
            }
        }

        return result
    }

    static BaseCommand parseCommand(Element command) {
        def type = command.getAttribute("type")

        if ("gradle".equals(type)) {
            return parseGradleCommand(command)
        } else if ("maven".equals(type)) {
            return parseMavewnCommand(command)
        } else {
            throw new RuntimeException("Unknown command type '$type'")
        }
    }

    static GradleCommand parseGradleCommand(Element element) {
        def result = new GradleCommand()
        def utility = element.getAttribute("utility")
        if (utility != null) {
            result.utility = utility
        }
        result.command = getOnlyElementTextContent(element, "value")

        return result
    }

    static MavenCommand parseMavewnCommand(Element element) {
        def result = new MavenCommand()
        def utility = element.getAttribute("utility")
        if (utility != null) {
            result.utility = utility
        }
        result.command = getOnlyElementTextContent(element, "value")

        return result
    }

}
