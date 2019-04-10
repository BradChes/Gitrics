package models.configs

data class Options(val repoPath: String,
                   val branchMinimumThreshold: Int,
                   val branchMaximumThreshold: Int,
                   val branchesAverageLifetimeMinimumThreshold: Int,
                   val branchesAverageLifetimeMaximumThreshold: Int,
                   val staleDefinition: Int)