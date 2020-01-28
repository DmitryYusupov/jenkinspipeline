import pipeline.stages.build.config.BuildStageConfig

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {
        if (stageConfig.hasCommands()) {
            println("HAS COMMANDS")
            if (stageConfig.hasGradleCommands()) {
                println("HAS GRADLE")
                buildToolsGradle.call(stageConfig.getGradleCommands())
            }

            if (stageConfig.hasMavenCommands()) {
            }

        } else {
            throw new RuntimeException("No commands");
        }
    }
}

