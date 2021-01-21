package git.jluvisi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
 * getMessage() method.
 */
public enum Messages {

    MUST_BE_PLAYER("messages.must-be-player"), NO_PERMISSION("messages.no-permission"),
    GAME_SIGNS_DISABLED("messages.game-signs-disabled"), GAME_SIGNS_LINE1("game-signs.line1-color"),
    GAME_SIGNS_LINE2("game-signs.line2"), GAME_SIGNS_LINE3("game-signs.line3"), GAME_SIGNS_LINE4("game-signs.line4"),
    GAME_FULL("messages.game-full"), IN_GAME_ALREADY("messages.already-in-game"),
    PLAYER_LOBBY_JOIN("messages.player-lobby-join"), GLOBAL_PLAYER_LOBBY_JOIN("messages.global-player-lobby-join"),
    GAME_STARTING("messages.game-starting"), PLAYERS_NEEDED("messages.players-needed");

    /**
     * We use a static singleton reference here instead of dependency injection
     * because the enum is a static class.
     */
    private static final ConfigManager configYAML = new ConfigManager(JavaPlugin.getPlugin(MicroGames.class),
            "config.yml");

    private String node;

    Messages(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return node;
    }

    /**
     * Replaces all placeholders in a string which was retrieved from the
     * configuration file. This method will replace all placeholders with their
     * respective values.
     *
     * @apiNote {@code replaceValue} must have keys be placeholders and values be
     *          the data to replace with.
     *
     * @param value        (String to replace placeholders on)
     * @param replaceValue (Actual placeholder, New Value)
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
     * @return BaseComponent[]
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
