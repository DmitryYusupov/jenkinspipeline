import pipeline.stages.build.config.BuildStageConfig

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {
        def gradleCommand = stageConfig.gradleCommand
        if (gradleCommand != null && gradleCommand.isEmpty()) {
            buildToolsGradle.call(gradleCommand)
        }


    }
}