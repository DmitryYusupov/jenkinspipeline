package utils.os.process

import utils.CollectionUtils

class ProcessOutput {
    List<String> output
    List<String> errorOutput

    String outputAsString() {
        if (CollectionUtils.isNotEmpty(output)) {
            return outputAsSingleString(output)
        }

        return null
    }


    String errorOutputAsString() {
        if (CollectionUtils.is(errorOutput)) {
            return outputAsSingleString(errorOutput)
        }

        return null
    }

    private String outputAsSingleString(List<String> output) {
        StringBuilder stringBuilder = new StringBuilder()

        for (String s : output) {
            stringBuilder.append(s)
        }
        return stringBuilder.toString();
    }
}
