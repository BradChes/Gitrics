package models

data class Account(var username: String, var password: String, var repoUri: String) {
    // These need to be a Git username, personal access token, and either a public or private https repository address.
    companion object {
        const val USERNAME = ""
        const val PASSWORD = ""
        const val REMOTE_REPO_URI = ""
    }

    init {
        username = USERNAME
        password = PASSWORD
        repoUri = REMOTE_REPO_URI
    }
}