package pipeline.stages.build.config

import pipeline.stages.common.commands.GradleCommand
import pipeline.stages.common.commands.MavenCommand
import pipeline.stages.common.utils.CollectionUtils

class BuildStageConfig {
    String stageName
    List<GradleCommand> gradleCommands
    List<MavenCommand> mavenCommands

    boolean hasCommands() {
        return (CollectionUtils.isNotEmpty(gradleCommands)
                || CollectionUtils.isNotEmpty(mavenCommands))
    }

    boolean hasMavenCommands() {
        return CollectionUtils.isNotEmpty(mavenCommands);
    }

    boolean hasGradleCommands() {
        return CollectionUtils.isNotEmpty(gradleCommands);
    }


}
