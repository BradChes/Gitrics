package models

data class Branch (val name: String,
                   val firstCreation: String?,
                   val isMerged: Boolean?,
                   val isStale: Boolean)