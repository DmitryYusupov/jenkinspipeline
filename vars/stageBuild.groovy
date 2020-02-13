import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.common.commands.BaseCommand
import pipeline.stages.common.commands.GradleCommand
import  pipeline.stages.common.stage.PipelineContext

def doBuild(BuildStageConfig stageConfig, PipelineContext context) {
    println("============================BEGIN $stageConfig.label ============================")

    stage(stageConfig.label) {
        if (stageConfig.hasCommands()) {

            println("AAA")

            for (BaseCommand command: stageConfig.getCommands()){
                println("BB")
                if(command instanceof GradleCommand){
                    println("CC")
                    buildToolsGradle.call(command as GradleCommand)
                }
            }
        } else {
            throw new RuntimeException("No commands");
        }
    }
    println("============================END $stageConfig.label ============================")
}

