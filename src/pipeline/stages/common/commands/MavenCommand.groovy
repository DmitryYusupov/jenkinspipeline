package pipeline.stages.common.commands

class MavenCommand extends BaseCommand {
    private static final UTILITY_NAME = "mvn"

    MavenCommand() {
        this.utility = UTILITY_NAME;
    }
}
