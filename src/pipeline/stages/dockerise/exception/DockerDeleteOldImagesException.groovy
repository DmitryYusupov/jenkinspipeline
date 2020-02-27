package pipeline.stages.dockerise.exception

class DockerDeleteOldImagesException extends DockeriseStageException {
    DockerDeleteOldImagesException() {
    }

    DockerDeleteOldImagesException(Throwable cause) {
        super(cause)
    }

    DockerDeleteOldImagesException(String message) {
        super(message)
    }
}
