package models

data class Branch (val name: String,
                   val firstCreation: String?,
                   val lastCommit: String?,
                   val isMerged: Boolean?,
                   val isStale: Boolean)