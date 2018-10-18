import services.GsonService
import services.JGitService

fun main(args : Array<String>) {
    App()
}

class App {

    private val jGitService: JGitService
    private val gsonService: GsonService

    init {
        val remoteRepositoryUri = Account.REMOTE_REPO_URI
        jGitService = JGitService(remoteRepositoryUri)
        gsonService = GsonService()

        val json = gsonService.branchesObjectToJson(jGitService.createBranchesObject())

        System.out.println(json)
    }
}