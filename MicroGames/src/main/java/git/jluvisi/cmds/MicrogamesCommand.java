package git.jluvisi.cmds;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
import git.jluvisi.minigames.GamePlayer;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Messages;
import git.jluvisi.util.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * Handles the main commands inside of the plugin. {@code /microgames} TODO:
 * /microgames invite
 */
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
                                        if (!sender.hasPermission(Permissions.RELOAD_CONFIG.toString())) {
                                                sender.spigot().sendMessage(Messages.NO_PERMISSION.getMessage());
                                                return true;
                                        }
                                        reload(sender);
                                        return true;
                                } else if (args[0].equalsIgnoreCase("setup")) {
                                        if (!sender.hasPermission(Permissions.SETUP_SIGN.toString())) {
                                                sender.spigot().sendMessage(Messages.NO_PERMISSION.getMessage());
                                                return true;
                                        }
                                        setupCommand(sender, args);
                                        return true;
                                } else if (args[0].equalsIgnoreCase("begin")) {

                                        // Command Example: /microgames begin <name> <num players> <max players>
                                        // <winning
                                        // score> <start time> <announce>
                                        // Creates a static one time game where players can join.

                                        if (!sender.hasPermission(Permissions.BEGIN_GAME.toString())) {
                                                sender.spigot().sendMessage(Messages.NO_PERMISSION.getMessage());
                                                return true;
                                        }

                                        beginCommand(sender, args);

                                        return true;
                                } else if (args[0].equalsIgnoreCase("join")) {
                                        // Command Example: /microgames join <lobby num>
                                        // Join a static game or a sign game with a command.

                                        if (!sender.hasPermission(Permissions.JOIN_GAME.toString())) {
                                                sender.spigot().sendMessage(Messages.NO_PERMISSION.getMessage());
                                                return true;
                                        }

                                        joinCommand(sender, args);

                                        return true;

                                } else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")) {

                                        // Command Example: /microgames leave
                                        // Leave the current game.
                                        if (!sender.hasPermission(Permissions.LEAVE_GAME.toString())) {
                                                sender.spigot().sendMessage(Messages.NO_PERMISSION.getMessage());
                                                return true;
                                        }
                                        leaveCommand(sender);
                                        return true;

                                } else if (args[0].equalsIgnoreCase("stop")) {

                                        // Command Example: /microgames stop <lobbyid>
                                        // Stops the game and kicks all players out.

                                } else if (args[0].equalsIgnoreCase("forcejoin")) {

                                        // Command Example: /microgames forcejoin <player> <lobbyid>
                                        // Force a player to join a game.

                                } else if (args[0].equalsIgnoreCase("joinset")) {
                                        // Command Example: /microgames joinset <player> <lobbyid> [game params]
                                        // Forces a player to join a game and if that game does not exist a non volatile
                                        // game is created with that name and params. Useful for things like CustomNPCs
                                        // and stuff.

                                }
                        }

                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "Unknown Argument for /microgames. Do /microgames help for a list of commands.")
                                                        .color(ChatColor.RED).create());
                        return true;

                }
                return false;

        }

        /**
         * Begin a Volatile game instance.
         *
         * @param sender
         */
        public void beginCommand(CommandSender sender, String[] args) {
                if (args.length != 7) {
                        sender.sendMessage(ChatColor.RED
                                        + "Incorrect Syntax. Use: /microgames begin <name> <num players> <max players> <winning score> <start time> <announce(true/false)>");
                        return;
                }

                // Setup parameters.
                String gameName; // arg 1
                int numMinPlayers;
                int numMaxPlayers;
                int winningScore;
                int startingTime;
                boolean announce;

                try {
                        gameName = args[1];
                        numMinPlayers = Integer.parseInt(args[2]);
                        numMaxPlayers = Integer.parseInt(args[3]);
                        winningScore = Integer.parseInt(args[4]);
                        startingTime = Integer.parseInt(args[5]);
                        announce = Boolean.parseBoolean(args[6]);
                        // Throw an exception if ANY of the criteria do not match.
                        if (!(GameInstance.gameIDRange.contains(gameName.length())
                                        && GameInstance.minPlayersRange.contains(numMinPlayers)
                                        && GameInstance.maxPlayersRange.contains(numMaxPlayers)
                                        && GameInstance.winningScoreRange.contains(winningScore)
                                        && GameInstance.startingTimeRange.contains(startingTime))) {
                                throw new Exception();
                        }

                } catch (Exception e) { // Catch any exception while attempting to parse all of these values.
                        sender.spigot().sendMessage(new ComponentBuilder().color(ChatColor.RED).append(
                                        "One or more of your arguments to identify this game were incorrect. Please repeat the command and try again.")
                                        .create());
                        return;
                }
                // There is not a game setup with that gameID yet.
                if (GameInstance.getByName(plugin.getGameInstances(), gameName) != null) {
                        sender.spigot().sendMessage(new ComponentBuilder("A game with this name already exists.")
                                        .color(ChatColor.RED).create());
                        return;
                }
                // They have permission to setup a game.
                if (!sender.hasPermission(Permissions.BEGIN_GAME.toString())) {
                        sender.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));
                        return;
                }
                final GameInstance instance = new GameInstance(gameName, null, numMinPlayers, numMaxPlayers,
                                startingTime, winningScore);
                plugin.getGameInstances().add(instance);
                // If the player has announcements on.
                if (announce) {
                        // Make a holder variable.
                        Player player = null;
                        // Get the message from config. We do this so we only need to use the
                        // replacePlaceholder method once.
                        final String message = Messages.replacePlaceholder(
                                        Messages.GAME_CREATED_NOTIFICATION.getLegacyMessage(),
                                        Messages.getPlaceholders(player, instance, StringUtils.EMPTY));
                        // Go through every player.
                        for (Player p : Bukkit.getOnlinePlayers()) {
                                // If they can recieve notification
                                if (!p.hasPermission(Permissions.NOTIFY.toString())) {
                                        continue;
                                }
                                // Set the holder variable to p.
                                player = p;
                                // Send them a message.
                                p.sendMessage(message);
                        }
                }
                // if they are a player executor we can add more placeholders.
                if (sender instanceof Player) {
                        sender.sendMessage(Messages.replacePlaceholder(Messages.GAME_CREATED_1.getLegacyMessage(),
                                        Messages.getPlaceholders((Player) sender, instance, StringUtils.EMPTY)));
                        sender.sendMessage(Messages.replacePlaceholder(Messages.GAME_CREATED_2.getLegacyMessage(),
                                        Messages.getPlaceholders((Player) sender, instance, StringUtils.EMPTY)));
                        return;
                }
                sender.sendMessage(Messages.replacePlaceholder(Messages.GAME_CREATED_1.getLegacyMessage(),
                                Messages.getPlaceholders(null, instance, StringUtils.EMPTY)));
                sender.sendMessage(Messages.replacePlaceholder(Messages.GAME_CREATED_2.getLegacyMessage(),
                                Messages.getPlaceholders(null, instance, StringUtils.EMPTY)));

        }

        /**
         * Command to join a game that is IN_LOBBY. Requires player.
         *
         * @see GameInstance
         * @param sender
         */
        public void joinCommand(CommandSender sender, String args[]) {
                Player p = null;
                if (sender instanceof Player) {
                        p = (Player) sender;
                } else {
                        sender.spigot().sendMessage(Messages.MUST_BE_PLAYER.getMessage());
                        return;
                }
                // microgames join <game name>
                if (args.length != 2) {
                        sender.sendMessage(ChatColor.RED + "Incorrect Syntax. Use: /microgames join <game name>");
                        return;
                }

                GameInstance instance = GameInstance.getByName(plugin.getGameInstances(), args[1]);
                if (instance == null) {
                        // We can use the wrong argument placeholder to send them a message.
                        sender.sendMessage(Messages.replacePlaceholder(Messages.GAME_CREATED_1.getLegacyMessage(),
                                        Messages.getPlaceholders(p, null, args[1])));
                        return;
                }
                // if the instance can be joined.
                if (instance.getPlayers().size() >= instance.getMaxPlayers()) {
                        p.spigot().sendMessage(new TextComponent(Messages.GAME_FULL.getMessage()));
                        return;
                }
                // The player has the permission.
                if (!p.hasPermission(Permissions.JOIN_GAME.toString())) {
                        p.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));
                        return;
                }

                // Check if the player is already in a game.
                if (instance.containsPlayer(p.getUniqueId())) {
                        p.spigot().sendMessage(new TextComponent(Messages.IN_GAME_ALREADY.getMessage()));
                        return;
                }

                // Check if a player is trying to join a different game.
                for (GameInstance game : plugin.getGameInstances()) {
                        if (game.containsPlayer(p.getUniqueId()) && game != instance) {
                                p.spigot().sendMessage(new TextComponent(Messages.MUST_LEAVE_GAME.getMessage()));
                                return;
                        }
                }
                // Add the player to the game instance.
                plugin.getGameInstances().get(plugin.getGameInstances().indexOf(instance))
                                .addPlayer(new GamePlayer(p.getUniqueId(), 0));
        }

        /**
         * Command to leave the game.
         *
         * @see GameInstance
         * @param sender
         */
        public void leaveCommand(CommandSender sender) {
                Player p = null;
                if (sender instanceof Player) {
                        p = (Player) sender;
                } else {
                        sender.spigot().sendMessage(Messages.MUST_BE_PLAYER.getMessage());
                        return;
                }
                final GameInstance instance = GameInstance.getPlayerGame(plugin.getGameInstances(), p.getUniqueId());
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

        /**
         * The reload command. Can take a CommandSender.
         *
         * @param sender
         */
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
                // If the number of arguments is correct.
                if (args.length != 2) {
                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "You must include a valid game name. Ex: /microgames setup lobby1")
                                                        .color(ChatColor.RED).create());
                        return;
                }
                // Between 4-12 characters
                if (args[1].length() > 12 || args[1].length() < 4) {
                        sender.spigot().sendMessage(new ComponentBuilder(
                                        "The name for the game must be between 12 and 4 characters.")
                                                        .color(ChatColor.RED).create());
                        return;
                }
                // There is not a game setup with that gameID yet.
                if (GameInstance.getByName(plugin.getGameInstances(), args[1]) != null) {
                        sender.spigot().sendMessage(new ComponentBuilder("A game with this name already exists.")
                                        .color(ChatColor.RED).create());
                        return;
                }
                // They have permission to setup a game.
                if (!p.hasPermission(Permissions.SETUP_SIGN.toString())) {
                        p.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));
                        return;
                }
                // If game signs are disabled.
                if (!configYAML.getBoolean("game-signs.enabled")) {
                        p.spigot().sendMessage(new TextComponent(Messages.GAME_SIGNS_DISABLED.getMessage()));
                        return;
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
