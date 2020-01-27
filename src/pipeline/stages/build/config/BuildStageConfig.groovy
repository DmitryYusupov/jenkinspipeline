package pipeline.stages.build.config

import org.apache.commons.lang3.StringUtils

class BuildStageConfig {
    String stageName
    String gradleCommand
    String mavenCommand

    Optional<BuildTool> getCommandBuildTool() {
        if (StringUtils.isNotBlank(this.gradleCommand)) {
            return Optional.of(BuildTool.GRADLE)
        } else if (StringUtils.isNotBlank(this.mavenCommand)) {
            return Optional.of(BuildTool.MAVEN)
        } else {
            return Optional.empty()
        }
    }

}
