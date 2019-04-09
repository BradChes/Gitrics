package models

data class ParsedConfig(val username: String,
                        val accessToken: String,
                        val repoUrls: List<String>,
                        val branchMinimum: Int,
                        val branchMaximum: Int,
                        val staleDefinition: Int)