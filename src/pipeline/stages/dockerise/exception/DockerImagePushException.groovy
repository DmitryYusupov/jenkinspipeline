package pipeline.stages.dockerise.exception

class DockerImagePushException extends DockeriseStageException {

    DockerImagePushException() {
    }

    DockerImagePushException(Exception e) {
        super(e)
    }

}
