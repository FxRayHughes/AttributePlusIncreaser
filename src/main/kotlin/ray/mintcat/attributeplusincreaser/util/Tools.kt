package ray.mintcat.attributeplusincreaser.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ray.mintcat.attributeplusincreaser.AttributePlusIncreaser
import java.util.*

object Tools {

    fun getPlayerList(): List<String> {
        return AttributePlusIncreaser.data.getKeys(false).toList()
    }

    fun getOfflinePlayer(name: String): OfflinePlayer? {
        val info = AttributePlusIncreaser.data.getString(name,"null")
        if (info == "null" || info == null){
            return null
        }
        return Bukkit.getOfflinePlayer(UUID.fromString(info))
    }

}