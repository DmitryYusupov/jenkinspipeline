package utils.os.process

final class ProcessUtils {
    private ProcessUtils() {

    }

    static ProcessOutput runProcessAndWaitForOutput(String command) {
        try {
            ProcessOutput output = new ProcessOutput()

            Process process = Runtime.getRuntime().exec(command)

            def outputReader = new ProcessOutputReader(process.getInputStream())
            new Thread(outputReader).start()

            def errorReader = new ProcessOutputReader(process.getErrorStream())
            new Thread(errorReader).start()

            process.waitFor()
            output.output = outputReader.output
            output.errorOutput = errorReader.output

            return output
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }

}
