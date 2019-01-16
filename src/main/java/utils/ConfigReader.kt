package utils

import models.Account

class ConfigReader(val ConfigPath: String) {

    fun jsonToAccount(): Account {
        return Account("","","")
    }
}