import pipeline.stages.common.stage.PipelineContext
import pipeline.stages.dockerise.config.AccessConfig
import pipeline.stages.dockerise.config.BuildImageConfig
import pipeline.stages.dockerise.config.DockeriseStageConfig
import pipeline.stages.dockerise.context.BuildImageStageContext
import pipeline.stages.dockerise.context.DockeriseStageContext

import utils.os.Os
import utils.os.OsUtils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Execute dockerisation: create, push images
 *
 * @param dockeriseConfig Config with data
 * @param pipelineContext Context which stores info about pipeline progress
 */
void doContainerisation(DockeriseStageConfig dockeriseConfig, PipelineContext pipelineContext) {
    println("============================BEGIN $dockeriseConfig.label ============================")
    stage(dockeriseConfig.label) {
        def image = buildImage(dockeriseConfig.buildImageConfig, pipelineContext)
        println("Image was build " + image)

        String tagName = dockeriseConfig.buildImageConfig.tagPrefix + "_" + env.BUILD_ID
        deleteImagesIfNumberOfStoredImagesHasExpired(
                dockeriseConfig.numberOfImagesToStore,
                dockeriseConfig.buildImageConfig.imageName,
                tagName,
                dockeriseConfig.buildImageConfig.tagPrefix
        )

        def accessConfig = dockeriseConfig.accessConfig
        if (accessConfig != null){
            def dockerImage = new DockerImage()
            dockerImage.name = dockeriseConfig.buildImageConfig.imageName
            dockerImage.tag = tagName
            pushToDockerRegistry(accessConfig, dockerImage)
        }
    }
    println("============================END $dockeriseConfig.label ============================")
}

/**
 * Create image
 *
 * @param buildImageConfig Contains information about image
 * @param pipelineContext Context which stores info about pipeline progress
 * @return image. Has type org.jenkinsci.plugins.docker.workflow.Docker$Image
 */
private def buildImage(BuildImageConfig buildImageConfig, PipelineContext pipelineContext) {

    def curStageContext = new DockeriseStageContext()
    def buildImageStageContext = new BuildImageStageContext()
    curStageContext.buildStageContext = buildImageStageContext

    curStageContext.buildStageContext
    pipelineContext.dockeriseStageContext = curStageContext

    def tagName = buildImageConfig.tagPrefix + "_" + env.BUILD_ID
    buildImageStageContext.tag = tagName
    buildImageStageContext.image = buildImageConfig.imageName

    def dockerFileDir = buildImageConfig.dockerFileDir
    if (dockerFileDir == null || dockerFileDir.isEmpty()) {
        def image = docker.build("$buildImageConfig.imageName:$tagName")
        return image
    } else {
        def image = docker.build("$buildImageConfig.imageName:$tagName", dockerFileDir)
        return image
    }
}

/**
 * Execute revert action for dockerise stage. For example, if error occurred we delete image which was build on this step
 *
 * @param exception
 * @param pipelineContext Context which stores info about pipeline progress
 */
void revertStageChanges(Exception exception, PipelineContext pipelineContext) {
    if (pipelineContext.dockeriseStageContext != null) {
        if (pipelineContext.dockeriseStageContext.buildStageContext != null) {
            revertBuildImageStageChanges(exception, pipelineContext.dockeriseStageContext.buildStageContext)
        }
    }
}
/**
 * Delete image if pipeline has finished with error
 *
 * @param exception
 * @param buildImageStageContext Context which stores information about docker stage
 */
private void revertBuildImageStageChanges(Exception exception, BuildImageStageContext buildImageStageContext) {
    println("----BEGIN ErrorHandling <DockeriseStage> ----")
    try {
        def imageToDelete = buildImageStageContext.image + ":" + buildImageStageContext.tag
        println("Try delete image '$imageToDelete'")
        osUtils.runCommand("docker rmi $buildImageStageContext.image:$buildImageStageContext.tag")
    } catch (Exception e) {
        println("Error while revert changes !! " + e.getMessage())
    }
    println("----END ErrorHandling <DockeriseStage> ----")
}

/**
 * Delete images if current number of stored images is greater than threshold value
 *
 * See on possible image info:
 * Full name: my-image:env_dev_1 (name: my-image, tag: env_dev_1, tag_prefix: env_dev_)
 *
 * @param maxImagesToStore threshold value, determines how many images we store
 * @param imageName Image name
 * @param imageTag Image tag
 * @param imageTagPrefix tag prefix
 *
 */
private void deleteImagesIfNumberOfStoredImagesHasExpired(int maxImagesToStore, String imageName, String imageTag, String imageTagPrefix) {
    println("-----------BEGIN. Dockerise. Clean old images-----------------")
    def command = getCommandToGetDockerImages(imageName, imageTag, imageTagPrefix)
    def output = osUtils.runCommandReturningOutput(command)
    List<DockerImage> images = parseDockerImagesDataFromOutputString(output, imageName)

    if (!images.isEmpty()) {
        deleteImageIfNeed(images.reverse(), maxImagesToStore)
    }
    println("-----------END. Dockerise. Clean old images-----------------")
}

/**
 * Return command which will return images with defined image name and tag
 *
 * See on possible image info:
 * Full name: my-image:env_dev_1 (name: my-image, tag: env_dev_1, tag_prefix: env_dev_)
 *
 * @param imageName Image name
 * @param imageTag Image tag
 * @param imageTagPrefix Image tag prefix
 *
 * @return command which will return images with defined image name and tag
 */
private String getCommandToGetDockerImages(String imageName, String imageTag, String imageTagPrefix) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            def command = "docker images --filter before=$imageName:$imageTag | find \"" + imageName + "\"";
            if (imageTagPrefix != null && !imageTagPrefix.isEmpty()) {
                command = command + "| find \"" + imageTagPrefix + "\"";
            }
            return command

        case Os.UNIX:
            def command = "docker images --filter before=$imageName:$imageTag | grep \"" + imageName + "\"";
            if (imageTagPrefix != null && !imageTagPrefix.isEmpty()) {
                command = command + "| grep \"" + imageTagPrefix + "\"";
            }
            return command

        default:
            throw new RuntimeException("Impossible to get dockerImages command. Unknown OS!")
    }
}


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
        return (name != null && !name.isEmpty()) && (tag != null && !tag.isEmpty()) && (id != null && !id.isEmpty());

    }
}
/**
 * We have output which contains data about docker image, for example:
 * 'My-image            env1_79                    5bc41458c38d        30 minutes ago      291MB'
 * 'other-image         env1_80                    5bc41458c3wd        32 minutes ago      291MB'
 *
 * Method parses output and convert it to objects which include imageName, imageTag and imageId
 *
 * @param outputStr output of docker images command
 * @param imageName. We want to return the only image with several tags.
 * @return objects which include imageName, imageTag and imageId
 */
private List<DockerImage> parseDockerImagesDataFromOutputString(String outputStr, String imageName) {
    List<DockerImage> result = new ArrayList<>()

    def regExp = "(\\s+)(\\w+)(\\s+)(\\w+)"
    Pattern pattern = Pattern.compile(regExp)

    def splited = outputStr.split("\n")
    for (int i = 0; i < splited.length; i++) {
        def imageInfoStr = splited[i].trim();
        if (imageInfoStr.startsWith(imageName)) {

            def dockerImage = new DockerImage()
            dockerImage.name = imageName
            imageInfoStr = imageInfoStr.replaceFirst(imageName, "")
            Matcher matcher = pattern.matcher(imageInfoStr)

            if (matcher.find() && matcher.groupCount() == 4) {
                dockerImage.tag = matcher.group(2)
                dockerImage.id = matcher.group(4)
            }

            result.add(dockerImage)
        }
    }

    return result
}

/**
 * Delete images if current number of images is greater or equals to threshold.
 * We want to store <threshold> images with defined image name and tag prefix
 *
 * @param images Images with predefined imagename and tag prefix, ordered by creationDate,
 * so the oldest are in the end.
 * @param threshold Desired number of images to store
 */
private void deleteImageIfNeed(List<DockerImage> images, int threshold) {
    if (images.size() >= threshold) {
        println("-----------BEGIN. Dockerise. Exec delete old images-----------------")
        int numberOfImagesToDelete = images.size() - threshold;
        for (int i = 0; i <= numberOfImagesToDelete; i++) {
            def image = images.get(i)
            def imageInfo = "$image.name:$image.tag $image.id"
            println("Try to delete image '$imageInfo'")
            def isSuccessfullyDeleted = osUtils.runCommandReturningStatusAsBool("docker rmi $image.id")
            if (isSuccessfullyDeleted) {
                println("Image '$imageInfo' was successfully deleted")
            } else {
                println("WARNING WARNING Image '$imageInfo' WAS NOT DELETED!")
            }
        }
        println("-----------END. Dockerise. Exec delete old images.-----------------")
    }
}
// Docker registry creation
// https://habr.com/ru/post/320884/
private void pushToDockerRegistry(AccessConfig accessConfig, DockerImage dockerImage) {
   // withCredentials([usernamePassword(credentialsId: "'" + accessConfig.login + "'", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        // available as an env variable, but will be masked if you try to print it out any which way
        // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
        // also available as a Groovy variable

        // or inside double quotes for string interpolation
     //   echo "username is $USERNAME"

    //}
    //docker login tools.adidas-group.com:5000 -u username -p password
    println("-----------BEGIN. Dockerise. Push image to registry-----------------")
    withCredentials([usernamePassword(credentialsId: "'" + accessConfig.login + "'", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        println(USERNAME)
        /*docker.withRegistry('registry.hub.docker.com/shop', "'" + USERNAME + "'") {
            //app.push("${env.BUILD_NUMBER}")
            //app.push("latest")
            docker.image("$imageName:$imageTag").push()
        }*/
        //  println("-----------BEGIN. Dockerise. Push image to registry-----------------")
        //}
    }
    println("-----------BEGIN. Dockerise. Push image to registry-----------------")
}



