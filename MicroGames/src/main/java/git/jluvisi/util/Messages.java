/** s */
package git.jluvisi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.MicroGames;
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
    /** <strong>Node:</strong> <i>messages.must-be-player </i> */
    MUST_BE_PLAYER("messages.must-be-player"),
    /** <strong>Node:</strong> <i>messages.no-permission </i> */
    NO_PERMISSION("messages.no-permission"),
    /** <strong>Node:</strong> <i>messages.game-signs.disabled </i> */
    GAME_SIGNS_DISABLED("messages.game-signs-disabled"),
    /** <strong>Node:</strong> <i> game-signs.line1-color </i> */
    GAME_SIGNS_LINE1("game-signs.line1-color"),
    /** <strong>Node:</strong> <i> game-signs.line2 </i> */
    GAME_SIGNS_LINE2("game-signs.line2"),
    /** <strong>Node:</strong> <i> game-signs.line3 </i> */
    GAME_SIGNS_LINE3("game-signs.line3"),
    /** <strong>Node:</strong> <i> game-signs.line4 </i> */
    GAME_SIGNS_LINE4("game-signs.line4"),
    /** <strong>Node:</strong> <i>messages.game-full </i> */
    GAME_FULL("messages.game-full"),
    /** <strong>Node:</strong> <i>messages.already-in-game </i> */
    IN_GAME_ALREADY("messages.already-in-game"),
    /** <strong>Node:</strong> <i>messages.player-lobby-join </i> */
    PLAYER_LOBBY_JOIN("messages.player-lobby-join"),
    /** <strong>Node:</strong> <i>messages.global-player-lobby-join </i> */
    GLOBAL_PLAYER_LOBBY_JOIN("messages.global-player-lobby-join"),
    /** <strong>Node:</strong> <i>messages.game-starting </i> */
    GAME_STARTING("messages.game-starting"),
    /** <strong>Node:</strong> <i>messages.players-needed </i> */
    PLAYERS_NEEDED("messages.players-needed"),
    /** <strong>Node:</strong> <i>messages.must-leave-game </i> */
    MUST_LEAVE_GAME("messages.must-leave-game"),
    /** <strong>Node:</strong> <i>messages.global-player-leave </i> */
    GLOBAL_LEAVE_GAME("messages.global-player-leave"),
    /** <strong>Node:</strong> <i>messages.leave-game </i> */
    LEAVE_GAME("messages.leave-game"),
    /** <strong>Node:</strong> <i>messages.player-cant-leave </i> */
    PLAYER_CANT_LEAVE("messages.player-cant-leave"),
    /** <strong>Node:</strong> <i>game-signs.game-full-color </i> */
    GAME_SIGNS_GAME_FULL_COLOR("game-signs.game-full-color"),
    /** <strong>Node:</strong> <i>game-signs.game-full-color-line </i> */
    GAME_SIGNS_GAME_COLOR_LINE("game-signs.game-full-color-line");

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
