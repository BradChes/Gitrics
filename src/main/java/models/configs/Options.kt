package models.configs

data class Options(val repoPath: String,
                   val branchMinimum: Int,
                   val branchMaximum: Int,
                   val staleDefinition: Int)