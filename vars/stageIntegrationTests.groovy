import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.commands.GradleCommand
import pipeline.stages.integrationtest.config.IntegrationTestsStageConfig

def doIntegrationTests(IntegrationTestsStageConfig stageConfig) {
    println("============================BEGIN $stageConfig.label ============================")
    stage(stageConfig.label) {
        if (stageConfig.hasCommands()) {

            for (BaseCommand command: stageConfig.getCommands()){

                if(command instanceof GradleCommand){
                    buildToolsGradle.call(command as GradleCommand)
                }

            }
        } else {
            throw new RuntimeException("No commands");
        }
    }
    println("============================END $stageConfig.label ============================")
}
