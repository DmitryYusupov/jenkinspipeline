package pipeline.stages.common.config

enum Stage {
    CHECKOUT("checkout"),
    BUILD("build")

    static Map<String, Stage> stagesByLabelMap = new HashMap<>();
    static {
        for (Stage stage : values()) {
            stagesByLabelMap.put(stage.label, stage)
        }
    }

    static Stage fromString(String s) {
        return stagesByLabelMap.get(s)
    }
    private String label

    Stage(String label) {
        this.label = label
    }

}