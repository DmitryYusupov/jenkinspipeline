void call(String command) {
    if (command == null)
        error 'gradle command MUST be defined'

    sh "gradle $command"
}