package models.configs

data class ParsedConfig(val username: String,
                        val accessToken: String,
                        val repoUrls: List<String>,
                        val repoPath: String,
                        val branchMinimumThreshold: Int,
                        val branchMaximumThreshold: Int,
                        val branchesAverageLifetimeMinimumThreshold: Int,
                        val branchesAverageLifetimeMaximumThreshold: Int,
                        val staleDefinition: Int)