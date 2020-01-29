import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.commands.GradleCommand

def doBuild(BuildStageConfig stageConfig) {
    println("============================BEGIN $stageConfig.stageName ============================")
    stage(stageConfig.stageName) {
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
    println("============================END $stageConfig.stageName ============================")
}

