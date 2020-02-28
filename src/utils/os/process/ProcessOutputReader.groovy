package utils.os.process

class ProcessOutputReader implements Runnable{
    private InputStream inputStream
    Exception error
    List<String> output;

    ProcessOutputReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    void run() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
            output = bufferedReader.readLines()
        } catch (Exception e) {
            error = e
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close()
            }
        }

    }
}
