package JGit

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository

class JGitService(private val repository: Repository) {

    private var git: Git = Git(repository)

}