package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import models.Account

class ConfigReader(val ConfigPath: String) {

    private val testJson =
    """
        {
            "username": "",
            "accessToken": "",
            "repoUrl": ""
        }
    """

    fun jsonToAccount(): Account {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(testJson, Account::class.java)
    }
}
