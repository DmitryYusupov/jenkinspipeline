import pipeline.stages.build.config.BuildStageConfig

def doBuild(BuildStageConfig stageConfig) {
    stage(stageConfig.stageName) {
    }
}