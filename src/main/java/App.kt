import JGit.JGitService

fun main(args : Array<String>) {
    App()
}

class App {

    private val jGitService: JGitService

    init {
        val remoteRepositoryUri = "https://github.com/BradChes/kotlin-play.git"
        jGitService = JGitService(remoteRepositoryUri)
    }
}