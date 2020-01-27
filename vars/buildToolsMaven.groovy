void call(String command) {
    if (command == null)
        error 'maven command MUST be defined'

    bat "mvn $command"
}