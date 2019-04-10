package models.data

import java.time.LocalDateTime

data class Branch (val name: String,
                   val firstCreation: LocalDateTime?,
                   val lastCommit: LocalDateTime?,
                   val isMerged: Boolean,
                   val isStale: Boolean)