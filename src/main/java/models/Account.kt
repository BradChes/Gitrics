package models

data class Account(val username: String,
                   val accessToken: String,
                   val repoUrl: String,
                   val branchMinimum: Int,
                   val branchMaximum: Int)