package pipeline.stages.common.config

enum Stage {
    CHECKOUT("checkout")

    private String stageName

    Stage(String stageName) {
        this.stageName = stageName
    }

    String getStageName() {
        return stageName
    }
}