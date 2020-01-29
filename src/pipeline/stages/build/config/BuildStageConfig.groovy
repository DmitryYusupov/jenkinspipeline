package pipeline.stages.build.config

import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.stage.BaseStageConfig
import pipeline.stages.common.utils.CollectionUtils

class BuildStageConfig extends BaseStageConfig{
    List<BaseCommand> commands

    boolean hasCommands() {
        return CollectionUtils.isNotEmpty(commands)
    }

}
