import pipeline.stages.build.config.BuildStageConfig

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {
        if (stageConfig.hasCommands()) {

            if (stageConfig.hasGradleCommands()) {
                buildToolsGradle.call(stageConfig.getGradleCommands())
            }

            if (stageConfig.hasMavenCommands()) {
            }

        } else {
            throw new RuntimeException("No commands");
        }
    }
}

