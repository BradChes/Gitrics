package models

data class BranchesLifetime(val allLifetime: Int,
                            val featLifetime: Int,
                            val spikeLifetime: Int,
                            val fixLifetime: Int)