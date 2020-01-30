package pipeline.configreader.common

import org.w3c.dom.Element
import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.commands.GradleCommand
import pipeline.stages.common.commands.MavenCommand

import static utils.XmlDomUtils.getOnlyElementTextContent

final class CommandsReader {
    private CommandsReader(){

    }

    static List<BaseCommand> parseCommands(Element commandsNode){
        List<BaseCommand> result = new ArrayList<>()
        def commands = commandsNode.getElementsByTagName("command")

        for (int i = 0; i < commands.length; i++) {
            def command = (Element) commands.item(i);
            result.add(parseCommand(command))
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
