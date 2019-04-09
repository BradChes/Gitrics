package services

interface OptionsService {
    fun createOptionsObject(): Options
}

class ParsedOptionsService: OptionsService {
    override fun createOptionsObject(): Options {
        return Options("repoUrl", 5, 15)
    }

}

data class Options(val repoUrl: String,
                   val branchMinimum: Int,
                   val branchMaximum: Int)