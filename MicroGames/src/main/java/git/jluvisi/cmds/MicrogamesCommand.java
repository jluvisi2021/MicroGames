package git.jluvisi.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import git.jluvisi.MicroGames;
import git.jluvisi.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;

public class MicrogamesCommand implements CommandExecutor {

    // Initalize Globals
    private final MicroGames plugin;
    private final ConfigManager configYAML;
    private final ConfigManager dataYAML;

    public MicrogamesCommand(MicroGames plugin) {
        this.plugin = plugin;
        this.configYAML = new ConfigManager(plugin, "config.yml");
        this.dataYAML = new ConfigManager(plugin, "data.yml");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("microgames")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    sendInfoMessage(p);
                    return true;
                } else if (args.length == 1) {
                    // Reload Config Command
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (!p.hasPermission(configYAML.getString("permissions.reload-config"))) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    configYAML.getString("messages.no-permission")));
                            return true;
                        }
                        p.sendMessage(ChatColor.GREEN + "Reloading Config...");
                        configYAML.reloadConfig();
                        dataYAML.reloadConfig();
                        return true;
                    }
                }

                p.sendMessage(ChatColor.RED
                        + "Unknown Argument for /microgames. Do /microgames help for a list of commands.");

            } else {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', configYAML.getString("messages.must-be-player")));
            }
            return true;
        }
        return false;
    }

    private void sendInfoMessage(Player p) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-"));
        p.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + ChatColor.ITALIC.toString()
                + "MicroGames");
        p.sendMessage(ChatColor.LIGHT_PURPLE + "Pl. Version: " + ChatColor.GRAY + plugin.getDescription().getVersion());
        p.sendMessage(ChatColor.LIGHT_PURPLE + "Author: " + ChatColor.GRAY + "Interryne");
        p.sendMessage(
                ChatColor.LIGHT_PURPLE + "Website: " + ChatColor.GRAY + "https://github.com/jluvisi2021/MicroGames");
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-"));
    }

}
