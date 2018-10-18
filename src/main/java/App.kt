import services.JGitService

fun main(args : Array<String>) {
    App()
}

class App {

    private val jGitService: JGitService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
    }
}