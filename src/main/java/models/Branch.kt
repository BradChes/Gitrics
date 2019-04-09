package models

import java.util.*

data class Branch (val name: String,
                   val firstCreation: Date?,
                   val lastCommit: Date?,
                   val isMerged: Boolean?,
                   val isStale: Boolean)