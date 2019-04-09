package models

data class Account(val username: String,
                   val accessToken: String,
                   val repoUrls: List<String>)