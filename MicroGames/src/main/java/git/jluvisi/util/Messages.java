/** s */
package git.jluvisi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * Manage the messages in the plugin. This class stores the paths in the config
 * as enums which can then be converted from enums to BaseComponent[] from the
 * getMessage() method. <br>
 * </br>
 * Get a message using the {@code getMessage()}.<br>
 * Raw, string messages can be obtained through {@code getLegacyMessage()}</br>
 *
 * <pre>
 * Example:
 * {@code
 * player.spigot().sendMessage(new TextComponent(Messages.MUST_BE_PLAYER.getMessage()))
 * }
 * </pre>
 */
public enum Messages {
    /** <strong>Node:</strong> <i>messages.general.must-be-player </i> */
    MUST_BE_PLAYER("messages.general.must-be-player"),
    /** <strong>Node:</strong> <i>messages.general.no-permission </i> */
    NO_PERMISSION("messages.general.no-permission"),
    /** <strong>Node:</strong> <i>messages.general.game-signs.disabled </i> */
    GAME_SIGNS_DISABLED("messages.general.game-signs-disabled"),
    /** <strong>Node:</strong> <i> game-signs.line1-color </i> */
    GAME_SIGNS_LINE1("game-signs.line1-color"),
    /** <strong>Node:</strong> <i> game-signs.line2 </i> */
    GAME_SIGNS_LINE2("game-signs.line2"),
    /** <strong>Node:</strong> <i> game-signs.line3 </i> */
    GAME_SIGNS_LINE3("game-signs.line3"),
    /** <strong>Node:</strong> <i> game-signs.line4 </i> */
    GAME_SIGNS_LINE4("game-signs.line4"),
    /** <strong>Node:</strong> <i>messages.lobby.game-full </i> */
    GAME_FULL("messages.lobby.game-full"),
    /** <strong>Node:</strong> <i>messages.lobby.already-in-game </i> */
    IN_GAME_ALREADY("messages.lobby.already-in-game"),
    /** <strong>Node:</strong> <i>messages.lobby.player-lobby-join </i> */
    PLAYER_LOBBY_JOIN("messages.lobby.player-lobby-join"),
    /** <strong>Node:</strong> <i>messages.lobby.global-player-lobby-join </i> */
    GLOBAL_PLAYER_LOBBY_JOIN("messages.lobby.global-player-lobby-join"),
    /** <strong>Node:</strong> <i>messages.lobby.game-starting </i> */
    GAME_STARTING("messages.lobby.game-starting"),
    /** <strong>Node:</strong> <i>messages.lobby.players-needed </i> */
    PLAYERS_NEEDED("messages.lobby.players-needed"),
    /** <strong>Node:</strong> <i>messages.lobby.must-leave-game </i> */
    MUST_LEAVE_GAME("messages.lobby.must-leave-game"),
    /** <strong>Node:</strong> <i>messages.lobby.global-player-leave </i> */
    GLOBAL_LEAVE_GAME("messages.lobby.global-player-leave"),
    /** <strong>Node:</strong> <i>messages.lobby.leave-game </i> */
    LEAVE_GAME("messages.lobby.leave-game"),
    /** <strong>Node:</strong> <i>messages.lobby.player-cant-leave </i> */
    PLAYER_CANT_LEAVE("messages.lobby.player-cant-leave"),
    /** <strong>Node:</strong> <i>game-signs.game-full-color </i> */
    GAME_SIGNS_GAME_FULL_COLOR("game-signs.game-full-color"),
    /** <strong>Node:</strong> <i>game-signs.game-full-color-line </i> */
    GAME_SIGNS_GAME_COLOR_LINE("game-signs.game-full-color-line"),
    /** <strong>Node:</strong> <i>messages.game.could-not-find-game </i> */
    GAME_NOT_FOUND("messages.game.could-not-find-game"),
    /** <strong>Node:</strong> <i>messages.game.game-created-notify </i> */
    GAME_CREATED_NOTIFICATION("messages.game.game-created-notify"),
    /** <strong>Node:</strong> <i>messages.game.game-created-1 </i> */
    GAME_CREATED_1("messages.game.game-created-1"),
    /** <strong>Node:</strong> <i>messages.game.game-created-2 </i> */
    GAME_CREATED_2("messages.game.game-created-2"),
    /**
     * <strong>Node:</strong> <i>messages.lobby.game-in-progress-or-disabled </i>
     */
    GAME_IN_PROGRESS("messages.lobby.game-in-progress-or-disabled");

    /**
     * We use a static singleton reference here instead of dependency injection
     * because the enum is a static class.
     *
     */
    private static final ConfigManager configYAML = new ConfigManager(JavaPlugin.getPlugin(MicroGames.class),
            "config.yml");

    /** The actual path to the specific config value from the enum. */
    private final String node;

    Messages(String node) {
        this.node = node;
    }

    /**
     * Returns a placeholder map of 4 placeholders. This placeholder map can be used
     * with only a player object specified. <br>
     * </br>
     * <ul>
     * <li>%player%</li>
     * <li>%world%</li>
     * <li>%health%</li>
     * <li>%hunger%</li>
     * </ul>
     * <br>
     * </br>
     *
     * @param p
     * @return
     */
    public static LinkedHashMap<String, String> getPlaceholders(Player p) {
        final LinkedHashMap<String, String> placeHolderMap = new LinkedHashMap<String, String>() {

            private static final long serialVersionUID = -961282036576650769L;

            {
                put("%player%", p.getDisplayName());
                put("%world%", p.getLocation().getWorld().getName());
                put("%health%", String.valueOf(p.getHealth()));
                put("%hunger%", String.valueOf(p.getFoodLevel()));
            }
        };
        return placeHolderMap;
    }

    /**
     * Returns a placeholder map of up to 10 placeholders. This placeholder map
     * repersents every placeholder that can be communicated through chat. If you
     * want to turn off a specific return value of this placeholder then set one of
     * the params to null.<br>
     * </br>
     * <ul>
     * <li>%player%</li>
     * <li>%world%</li>
     * <li>%health%</li>
     * <li>%hunger%</li>
     * <li>%time_left%</li>
     * <li>%players_needed%</li>
     * <li>%players_in_lobby%</li>
     * <li>%max_players_allowed%</li>
     * <li>%game_name%</li>
     * <li>%argument_error%</li>
     * </ul>
     * <br>
     * </br>
     *
     * @param p
     * @param gp
     * @param badArgument
     * @return
     */
    public static LinkedHashMap<String, String> getPlaceholders(Player p, GameInstance instance, String badArgument) {
        final LinkedHashMap<String, String> placeHolderMap = new LinkedHashMap<String, String>() {

            private static final long serialVersionUID = -4426899001752269913L;

            {
                if (p != null) {
                    put("%player%", p.getPlayer().getDisplayName());
                    put("%world%", p.getPlayer().getLocation().getWorld().getName());
                    put("%health%", String.valueOf(p.getPlayer().getHealth()));
                    put("%hunger%", String.valueOf(p.getPlayer().getFoodLevel()));
                }
                if (instance != null) {
                    put("%time_left%", String.valueOf(instance.getRemaningTime()));
                    put("%players_needed%", String.valueOf((instance.getMinPlayers() - instance.getPlayers().size())));
                    put("%players_in_lobby%", String.valueOf(instance.getPlayers().size()));
                    put("%max_players_allowed%", String.valueOf(instance.getMaxPlayers()));
                    put("%game_name%", instance.getGameInstanceID());
                }
                if (badArgument != StringUtils.EMPTY) {
                    put("%argument_error%", badArgument);
                }
            }
        };
        return placeHolderMap;
    }

    /**
     * Returns the raw node without any config parsing.
     */
    @Override
    public String toString() {
        return node;
    }

    /**
     * Retrieve a specific value inside of the messages enum.
     *
     * @param str
     * @return
     */
    public static Messages fromString(String str) {
        if (Messages.valueOf(str) != null) {
            return Messages.valueOf(str);
        }
        throw new NullPointerException();
    }

    /**
     * Replaces all placeholders in a string which was retrieved from the
     * configuration file. This method will replace all placeholders with their
     * respective values.
     *
     * @apiNote {@code replaceValue} uses a {@link LinkedHashMap} where the keys are
     *          expected to be the placeholders and the values are expected to be
     *          the data to replace the placeholders with.
     *
     * @param value        (String to replace placeholders on)
     * @param replaceValue (Actual placeholder, New Value)
     *
     *                     Set the placeholders of this hashmap like so:
     *
     *                     <pre>
     *
     *                     final LinkedHashMap<String, String> placeHolderMap = new LinkedHashMap<String, String>() {
     *                         {
     *                             put("%placeholder%", String.valueOf(configString));
     *
     *                         }
     *                     };
     *
     *                     </pre>
     *
     * @return replaced string
     */
    public static String replacePlaceholder(String value, LinkedHashMap<String, String> replaceValue) {
        for (Map.Entry<String, String> entry : replaceValue.entrySet()) {
            String placeholder = entry.getKey();
            String data = entry.getValue();
            value = StringUtils.replace(value, placeholder, data);
        }
        return value;
    }

    /**
     * Get the configuration message for the specified Enum. Does account for
     * ChatColors.
     *
     * {@code BaseComponent[]} can be converted to {@code TextComponents} through
     * the {@code TextComponent(BaseComponent[])} constructor.
     *
     * @return {@code BaseComponent[]}
     */
    public BaseComponent[] getMessage() {
        return TextComponent
                .fromLegacyText(ChatColor.translateAlternateColorCodes('&', configYAML.getString(node.toString())));
    }

    /**
     * Add a Hover Event to a String.
     *
     * @param message
     * @param hoverText
     * @return
     */
    public TextComponent addHoverEvent(String message, String hoverText) {
        TextComponent mComponent = new TextComponent(TextComponent.fromLegacyText(message));
        mComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(hoverText)));
        return mComponent;
    }

    /**
     * Add a text URL (Click Event) to a string.
     *
     * @param message
     * @param url
     * @return
     */
    public TextComponent addTextURL(String message, String url) {
        TextComponent mComponent = new TextComponent(TextComponent.fromLegacyText(message));
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, (url)));
        return mComponent;
    }

    /**
     * Add a clickable text event that runs a command.
     *
     * @param message
     * @param command
     * @return
     */
    public TextComponent addCommand(String message, String command) {
        TextComponent mComponent = new TextComponent(TextComponent.fromLegacyText(message));
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, (command)));
        return mComponent;
    }

    /**
     * Add a Hover Event to a String.
     *
     * @param message
     * @param hoverText
     * @return
     */
    public TextComponent addHoverEvent(BaseComponent[] message, String hoverText) {
        TextComponent mComponent = new TextComponent(message);
        mComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(hoverText)));
        return mComponent;
    }

    /**
     * Add a text URL (Click Event) to a string.
     *
     * @param message
     * @param url
     * @return
     */
    public TextComponent addTextURL(BaseComponent[] message, String url) {
        TextComponent mComponent = new TextComponent(message);
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, (url)));
        return mComponent;
    }

    /**
     * Add a clickable text event that runs a command.
     *
     * @param message
     * @param command
     * @return
     */
    public TextComponent addCommand(BaseComponent[] message, String command) {
        TextComponent mComponent = new TextComponent(message);
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, (command)));
        return mComponent;
    }

    /**
     * Add a hover event to a piece of text.
     *
     * @param mComponent
     * @param hoverText
     * @return
     */
    public TextComponent addHoverEvent(TextComponent mComponent, String hoverText) {
        mComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(hoverText)));
        return mComponent;
    }

    /**
     * Add a click event url to a piece of text.
     *
     * @param mComponent
     * @param url
     * @return
     */
    public TextComponent addTextURL(TextComponent mComponent, String url) {
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, (url)));
        return mComponent;
    }

    /**
     * Add a command to a piece of text.
     *
     * @param mComponent
     * @param command
     * @return
     */
    public TextComponent addCommand(TextComponent mComponent, String command) {
        mComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, (command)));
        return mComponent;
    }

    /**
     * Get the string as a chatcolor string.
     *
     * @return
     */
    public String getLegacyMessage() {
        return ChatColor.translateAlternateColorCodes('&', configYAML.getString(node.toString()));
    }

}

enum PlaceholderType {
    GENERAL, LOBBY, GAME
}
