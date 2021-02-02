package ray.mintcat.attributeplusincreaser

import io.izzel.taboolib.module.db.local.LocalPlayer
import io.izzel.taboolib.module.inject.TListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.serverct.ersha.api.AttributeAPI
import org.serverct.ersha.api.event.AttrUpdateAttributeEvent
import org.serverct.ersha.attribute.data.AttributeData

@TListener
class Listener : Listener {

    @EventHandler
    fun onPlayerAttr(event: AttrUpdateAttributeEvent.Before) {
        val player = event.attributeData.entity
        if (player !is Player) {
            return
        }
        val data = LocalPlayer.get(player)
        val list = data.getStringList("attributeplusincreaser.increase")
        Bukkit.getScheduler().runTaskAsynchronously(AttributePlusIncreaser.plugin, Runnable {
            AttributeAPI.getAPI().addSourceAttribute(event.attributeData, "apilasting", list)
        })
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        AttributePlusIncreaser.data.set(event.player.name, event.player.uniqueId.toString())
        AttributePlusIncreaser.data.saveToFile()
    }

}