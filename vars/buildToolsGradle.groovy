void call(String command) {
    println("WWWWWWWWWWWWW")
    println("Call " + command)
    if (command == null)
        error 'gradle command MUST be defined'

    sh "gradle $command"
}