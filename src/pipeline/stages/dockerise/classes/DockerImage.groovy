package pipeline.stages.dockerise.classes

class DockerImage {
    String name
    String tag
    String id

    @Override
    public String toString() {
        return "DockerImage{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    boolean isImageValid() {
        return (name != null && !name.isEmpty())  && (tag != null && !tag.isEmpty()) && (id != null && !id.isEmpty());

    }
}