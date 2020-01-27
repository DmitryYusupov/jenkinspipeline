import pipeline.stages.build.config.BuildStageConfig
import pipeline.stages.build.config.BuildTool
import pipeline.stages.common.exceptions.UnknownBuildToolException

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {

        Optional<BuildTool> buildTool = stageConfig.getCommandBuildTool()
        println("AAAAAAAAA")
        println(buildTool.get())
        println("BBBBBBBBB")
        if (buildTool.isPresent()) {

            switch (buildTool.get()) {
                case BuildTool.MAVEN:
                    buildToolsMaven.call(stageConfig.mavenCommand)
                    break
                case BuildTool.GRADLE:
                    buildToolsGradle.call(stageConfig.gradleCommand)
                    break
            }


        } else {
 //           throw new UnknownBuildToolException();
        }
    }
}