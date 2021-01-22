package git.jluvisi.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
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

                        if (args.length == 0) {

                                sendInfoMessage(sender);
                                return true;
                        } else {
                                // Reload Config Command
                                if (args[0].equalsIgnoreCase("reload")) {
                                        reload(sender);
                                        return true;
                                } else if (args[0].equalsIgnoreCase("setup")) {
                                        setupCommand(sender, args);
                                        return true;
                                } else if (args[0].equalsIgnoreCase("begin")) {

                                        // Command Example: /microgames begin <num players> <max players> <winning
                                        // score> <start time>
                                        // Creates a static one time game where players can join.

                                } else if (args[0].equalsIgnoreCase("join")) {

                                        // Command Example: /microgames join <lobby num>
                                        // Join a static game or a sign game with a command.

                                } else if (args[0].equalsIgnoreCase("leave")) {

                                        // Command Example: /microgames leave
                                        // Leave the current game.
                                        leaveCommand(sender);
                                        return true;

                                } else if (args[0].equalsIgnoreCase("stop")) {

                                        // Command Example: /microgames stop <lobbyid>
                                        // Stops the game and kicks all players out.

                                } else if (args[0].equalsIgnoreCase("forcejoin")) {

                                        // Command Example: /microgames forcejoin <player> <lobbyid>
                                        // Force a player to join a game.

                                }
                        }

                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "Unknown Argument for /microgames. Do /microgames help for a list of commands.")
                                                        .color(ChatColor.RED).create());
                        return true;

                }
                return false;

        }

        public void leaveCommand(CommandSender sender) {
                Player p = null;
                if (sender instanceof Player) {
                        p = (Player) sender;
                } else {
                        sender.spigot().sendMessage(Messages.MUST_BE_PLAYER.getMessage());
                        return;
                }
                GameInstance instance = GameInstance.getPlayerGame(p.getUniqueId());
                if (instance == null) {
                        p.spigot().sendMessage(Messages.PLAYER_CANT_LEAVE.getMessage());
                        return;
                }
                instance.removePlayer(GameInstance.getPlayer(instance, p.getUniqueId()));
        }

        /**
         * A message that tells the user some information about the plugin. Used when
         * the /microgames command has no arguments.
         *
         * @param onlineplayer
         */
        private void sendInfoMessage(CommandSender p) {

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

        public void reload(CommandSender sender) {
                if (!sender.hasPermission(Permissions.RELOAD_CONFIG.toString())) {
                        sender.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));

                }
                sender.spigot().sendMessage(
                                new ComponentBuilder("Reloading Config...").color(ChatColor.GREEN).create());

                configYAML.reloadConfig();
                signsYAML.reloadConfig();

        }

        /**
         * Must be a player.
         *
         * <br>
         * </br>
         * Gives the player a game sign. /microgames setup <game name>
         *
         * @param sender
         */
        public void setupCommand(CommandSender sender, String[] args) {
                Player p = null;
                if (sender instanceof Player) {
                        p = (Player) sender;
                } else {
                        sender.spigot().sendMessage(Messages.MUST_BE_PLAYER.getMessage());
                        return;
                }

                if (args.length != 2) {
                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "You must include a valid game name. Ex: /microgames setup lobby1")
                                                        .color(ChatColor.RED).create());
                        return;
                }

                if (args[1].length() > 12 || args[1].length() < 4) {
                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "The name for the game must be between 12 and 4 characters.")
                                                        .color(ChatColor.RED).create());
                        return;
                }

                if (GameInstance.getByName(args[1]) != null) {
                        sender.spigot().sendMessage(new ComponentBuilder("A game with this name already exists.")
                                        .color(ChatColor.RED).create());
                        return;
                }

                if (!p.hasPermission(Permissions.SETUP_SIGN.toString())) {
                        p.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));

                }
                // If game signs are disabled.
                if (!configYAML.getBoolean("game-signs.enabled")) {
                        p.spigot().sendMessage(new TextComponent(Messages.GAME_SIGNS_DISABLED.getMessage()));

                }
                // Setup the metadata for the player so we know when they enter
                // arguments in chat.

                // Add metadata to the player so that we can know they are setting up
                // the sign.
                p.setMetadata("setup-begin", new FixedMetadataValue(plugin, "setup-begin"));
                p.setMetadata("setup-game-name", new FixedMetadataValue(plugin, args[1]));
                p.spigot().sendMessage(new ComponentBuilder("Please type following arguments in one line.").italic(true)
                                .color(ChatColor.GRAY).append("\n").reset().color(ChatColor.BLUE)
                                .append("1. Number of players to start the game. (1-100)\n")
                                .append("2. Max number of players allowed. (2-100)\n")
                                .append("3. Winning Score for the game. (1-99)\n")
                                .append("4. Number of seconds until the game starts after players reach starting threshold. (10-300)\n")
                                .reset().color(ChatColor.BLUE).append("Example: ").color(ChatColor.DARK_AQUA)
                                .append("\"5 10 12 30\"").color(ChatColor.GRAY).create());
        }

}
