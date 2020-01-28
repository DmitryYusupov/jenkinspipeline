import org.apache.commons.collections4.CollectionUtils
import pipeline.stages.build.config.BuildStageConfig

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {
        if (stageConfig.hasCommands()) {

            if (CollectionUtils.isNotEmpty(stageConfig.mavenCommands)) {
                buildToolsGradle.call(stageConfig.getGradleCommands())
            }

            if (CollectionUtils.isNotEmpty(stageConfig.gradleCommands)) {

            }

        } else {
            throw new RuntimeException("No commands");
        }
    }
}

