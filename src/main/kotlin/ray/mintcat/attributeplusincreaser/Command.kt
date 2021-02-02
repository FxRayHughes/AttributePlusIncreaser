package ray.mintcat.attributeplusincreaser

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.module.db.local.LocalPlayer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.serverct.ersha.api.AttributeAPI
import ray.mintcat.attributeplusincreaser.util.Helper
import ray.mintcat.attributeplusincreaser.util.Tools
import java.util.*

@BaseCommand(name = "attributeplusincreaser", aliases = ["api"], permission = "*")
class Command : BaseMainCommand(), Helper {

    @SubCommand(description = "给予临时属性 [覆盖]")
    var abstemp: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"), Argument("属性"), Argument("时间[tick]"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0])
            if (player == null) {
                sender.error("目标不存在或离线.")
                return
            }
            AttributeAPI.getAPI().addSourceAttribute(AttributeAPI.getAPI().getAttrData(player), "Increase", listOf(args[1]))
            AttributeAPI.getAPI().runEntityTask(1000 * args[1].toLong(), "Increase", player) {
                AttributeAPI.getAPI().takeSourceAttribute(AttributeAPI.getAPI().getAttrData(player), "Increase")
            }
        }
    }

    @SubCommand(description = "给予临时属性 [不覆盖]")
    var temp: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"), Argument("属性"), Argument("时间[tick]"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0])
            if (player == null) {
                sender.error("目标不存在或离线.")
                return
            }
            val id = UUID.randomUUID().toString()
            AttributeAPI.getAPI().addSourceAttribute(AttributeAPI.getAPI().getAttrData(player), id, listOf(args[1]))
            AttributeAPI.getAPI().runEntityTask(1000 * args[1].toLong(), id, player) {
                AttributeAPI.getAPI().takeSourceAttribute(AttributeAPI.getAPI().getAttrData(player), id)
            }
        }
    }

    @SubCommand(description = "给予临时属性 [可选覆盖]")
    var othertemp: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"), Argument("属性"), Argument("时间[tick]"), Argument("属性编号"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayer(args[0])
            if (player == null) {
                sender.error("目标不存在或离线.")
                return
            }
            AttributeAPI.getAPI().addSourceAttribute(AttributeAPI.getAPI().getAttrData(player), args[3], listOf(args[1]))
            AttributeAPI.getAPI().runEntityTask(1000 * args[1].toLong(), args[3], player) {
                AttributeAPI.getAPI().takeSourceAttribute(AttributeAPI.getAPI().getAttrData(player), args[3])
            }
        }
    }

    @SubCommand(description = "给予永久属性 [不覆盖]")
    var lasting: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"){Tools.getPlayerList()}, Argument("属性"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Tools.getOfflinePlayer(args[0])
            if (player == null) {
                sender.error("目标不存在")
                return
            }
            val data = LocalPlayer.get(player)
            val list = data.getStringList("attributeplusincreaser.increase")
            list.add(args[1])
            data.set("attributeplusincreaser.increase",list)

            val target = Bukkit.getPlayer(args[0]) ?: return
            update(target)
        }
    }

    @SubCommand(description = "查看玩家的永久属性")
    var lastList: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"){Tools.getPlayerList()})
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Tools.getOfflinePlayer(args[0])
            if (player == null) {
                sender.error("目标不存在.")
                return
            }
            val data = LocalPlayer.get(player)
            val list = data.getStringList("attributeplusincreaser.increase")
            sender.info("玩家 &f${args[0]} 的永久属性有:")
            list.forEach {
                sender.info(" - $it")
            }
        }
    }

    @SubCommand(description = "删除玩家的永久属性")
    var lastingRemove: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"){Tools.getPlayerList()},Argument("属性"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Tools.getOfflinePlayer(args[0])
            if (player == null) {
                sender.error("目标不存在")
                return
            }
            val data = LocalPlayer.get(player)
            val list = data.getStringList("attributeplusincreaser.increase")
            list.remove(args[1])
            data.set("attributeplusincreaser.increase",list)

            val target = Bukkit.getPlayer(args[0]) ?: return
            update(target)
        }
    }

    @SubCommand(description = "清空玩家的永久属性")
    var lastingClear: BaseSubCommand = object : BaseSubCommand() {

        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标名称"){Tools.getPlayerList()},Argument("属性"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Tools.getOfflinePlayer(args[0])
            if (player == null) {
                sender.error("目标不存在")
                return
            }
            val data = LocalPlayer.get(player)
            data.set("attributeplusincreaser.increase",null)

            val target = Bukkit.getPlayer(args[0]) ?: return
            update(target)
        }
    }

    fun update(player: Player){
        Bukkit.getScheduler().runTaskAsynchronously(AttributePlusIncreaser.plugin, Runnable {
            AttributeAPI.getAPI().getAttrData(player).updateAttribute()
        })
    }
}