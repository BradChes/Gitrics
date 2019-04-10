package models.configs

data class Options(val repoPath: String,
                   val branchMinimum: Int,
                   val branchMaximum: Int,
                   val branchesAverageLifetimeMinimumThreshold: Int,
                   val branchesAverageLifetimeMaximumThreshold: Int,
                   val staleDefinition: Int)