package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiEnableCommand : CommandManager("enable", "habilitar") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        val pluginName = args[0]
        if (Mine.existsPlugin(sender, pluginName)) {
            val plugin = Mine.getPlugin(pluginName)
            sender.sendMessage("§bEduardAPI §aPlugin §2$pluginName§a foi ativado")
            Bukkit.getPluginManager().enablePlugin(plugin)
        }

        return true
    }

    init {
        usage = "/api enable <plugin>"
        description = "Ativa um plugin desativado no servidor"
    }
}