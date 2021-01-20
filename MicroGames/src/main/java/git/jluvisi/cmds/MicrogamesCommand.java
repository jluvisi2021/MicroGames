package git.jluvisi.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import git.jluvisi.MicroGames;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Messages;
import git.jluvisi.util.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class MicrogamesCommand implements CommandExecutor {

        // Initalize Globals
        private final MicroGames plugin;
        private final ConfigManager configYAML;
        private final ConfigManager signsYAML;

        public MicrogamesCommand(MicroGames plugin) {
                this.plugin = plugin;
                this.configYAML = new ConfigManager(plugin, "config.yml");
                this.signsYAML = new ConfigManager(plugin, "signs.yml");
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
                                                if (!p.hasPermission(Permissions.RELOAD_CONFIG.toString())) {
                                                        p.spigot().sendMessage(new TextComponent(
                                                                        Messages.NO_PERMISSION.getMessage()));
                                                        return true;
                                                }
                                                p.spigot().sendMessage(new ComponentBuilder("Reloading Config...")
                                                                .color(ChatColor.GREEN).create());

                                                configYAML.reloadConfig();
                                                signsYAML.reloadConfig();
                                                return true;
                                        } else if (args[0].equalsIgnoreCase("setup")) {
                                                if (!p.hasPermission(Permissions.SETUP_SIGN.toString())) {
                                                        p.spigot().sendMessage(new TextComponent(
                                                                        Messages.NO_PERMISSION.getMessage()));
                                                        return true;
                                                }
                                                // If game signs are disabled.
                                                if (!configYAML.getBoolean("game-signs.enabled")) {
                                                        p.spigot().sendMessage(new TextComponent(
                                                                        Messages.GAME_SIGNS_DISABLED.getMessage()));
                                                        return true;
                                                }
                                                // Setup the metadata for the player so we know when they enter
                                                // arguments in chat.

                                                // Add metadata to the player so that we can know they are setting up
                                                // the sign.
                                                p.setMetadata("setup-begin",
                                                                new FixedMetadataValue(plugin, "setup-begin"));
                                                p.spigot().sendMessage(new ComponentBuilder(
                                                                "Please type following arguments in one line.")
                                                                                .color(ChatColor.YELLOW).append("\n")
                                                                                .color(ChatColor.GRAY)
                                                                                .append("1. Number of players to start the game. (1-100)\n")
                                                                                .append("2. Max number of players allowed. (2-100)\n")
                                                                                .append("3. Winning Score for the game. (1-99)\n")
                                                                                .append("4. Number of seconds until the game starts after players reach starting threshold. (10-300)\n")
                                                                                .color(ChatColor.GRAY)
                                                                                .append("Example: ")
                                                                                .color(ChatColor.BLUE)
                                                                                .append("\"5 10 12 30\"")
                                                                                .color(ChatColor.RESET).create());
                                                return true;
                                        }
                                }

                                p.spigot().sendMessage(new ComponentBuilder(
                                                "Unknown Argument for /microgames. Do /microgames help for a list of commands.")
                                                                .color(ChatColor.RED).create());

                        } else {
                                sender.spigot().sendMessage(new TextComponent(Messages.MUST_BE_PLAYER.getMessage()));
                        }
                        return true;
                }
                return false;
        }

        /**
         * A message that tells the user some information about the plugin. Used when
         * the /microgames command has no arguments.
         *
         * @param onlineplayer
         */
        private void sendInfoMessage(Player p) {

                final String headerFooter = ChatColor.translateAlternateColorCodes('&',
                                "&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-&7-&5-");

                TextComponent infoMessage = new TextComponent();
                infoMessage.addExtra(new TextComponent(TextComponent.fromLegacyText(headerFooter)));
                infoMessage.addExtra("\n");
                infoMessage.addExtra(new TextComponent(new ComponentBuilder("MicroGames").color(ChatColor.LIGHT_PURPLE)
                                .bold(true).italic(true).create()));
                infoMessage.addExtra("\n");
                infoMessage.addExtra(new TextComponent(
                                new ComponentBuilder("Plugin Version: ").color(ChatColor.LIGHT_PURPLE).create()));
                infoMessage.addExtra(new TextComponent(new ComponentBuilder(plugin.getDescription().getVersion())
                                .color(ChatColor.GRAY).create()));
                infoMessage.addExtra("\n");
                infoMessage.addExtra(new TextComponent(
                                new ComponentBuilder("Config Version: ").color(ChatColor.LIGHT_PURPLE).create()));
                infoMessage.addExtra(
                                new TextComponent(new ComponentBuilder(configYAML.getString("major-config-version"))
                                                .color(ChatColor.GRAY).create()));
                infoMessage.addExtra("\n");
                infoMessage.addExtra(new TextComponent(
                                new ComponentBuilder("Author: ").color(ChatColor.LIGHT_PURPLE).create()));
                infoMessage.addExtra(
                                new TextComponent(new ComponentBuilder("Interryne").color(ChatColor.GRAY).create()));
                infoMessage.addExtra("\n");
                infoMessage.addExtra(new TextComponent(
                                new ComponentBuilder("Website: ").color(ChatColor.LIGHT_PURPLE).create()));

                TextComponent website = new TextComponent("CLICK HERE");
                website.setColor(ChatColor.YELLOW);
                website.setBold(true);
                website.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                "https://github.com/jluvisi2021/MicroGames"));
                website.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new Text(ChatColor.YELLOW + "Click to go to Github!")));
                website.addExtra("\n");

                infoMessage.addExtra(website);
                infoMessage.addExtra(new TextComponent(TextComponent.fromLegacyText(headerFooter)));

                p.spigot().sendMessage(infoMessage);
        }

}
