package pipeline.stages.common.config

enum Stage {
    CHECKOUT("checkout"),
    BUILD("build")

    static Map<String, Stage> stagesByLabelMap = new HashMap<>();
    static {
        for (Stage stage : values()) {
            stagesByLabelMap.put(stage.stageName, stage)
        }
    }

    static Optional<Stage> fromString(String s) {
        return Optional.ofNullable(stagesByLabelMap.get(s))
    }
    private String stageName

    Stage(String stageName) {
        this.stageName = stageName
    }

    String getStageName() {
        return stageName
    }
}