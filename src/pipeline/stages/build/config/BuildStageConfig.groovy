package pipeline.stages.build.config

import org.apache.commons.collections4.CollectionUtils
import pipeline.stages.common.commands.GradleCommand
import pipeline.stages.common.commands.MavenCommand

class BuildStageConfig {
    String stageName
    List<GradleCommand> gradleCommands
    List<MavenCommand> mavenCommands

    boolean hasCommands(){
        return CollectionUtils.isNotEmpty(gradleCommands) || CollectionUtils.isNotEmpty(mavenCommands);
    }
}
