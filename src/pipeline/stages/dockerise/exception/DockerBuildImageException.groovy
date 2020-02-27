package pipeline.stages.dockerise.exception

class DockerBuildImageException extends DockeriseStageException {
    DockerBuildImageException() {
    }

    DockerBuildImageException(Throwable cause) {
        super(cause)
    }

    DockerBuildImageException(String message) {
        super(message)
    }
}
