package pipeline.stages.integrationtest.config

import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.stage.BaseStageConfig
import utils.CollectionUtils

class IntegrationTestsStageConfig extends BaseStageConfig {
    List<BaseCommand> commands

    boolean hasCommands() {
        return CollectionUtils.isNotEmpty(commands)
    }

}