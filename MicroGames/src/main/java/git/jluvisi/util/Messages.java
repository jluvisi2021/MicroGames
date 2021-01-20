package git.jluvisi.util;

import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.MicroGames;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Manage the messages in the plugin. This class stores the paths in the config
 * as enums which can then be converted from enums to BaseComponent[] from the
 * getMessage() method.
 */
public enum Messages {

    MUST_BE_PLAYER("messages.must-be-player"), NO_PERMISSION("messages.no-permission"),
    GAME_SIGNS_DISABLED("messages.game-signs-disabled"), GAME_SIGNS_LINE1("game-signs.line1-color"),
    GAME_SIGNS_LINE2("game-signs.line2"), GAME_SIGNS_LINE3("game-signs.line3"), GAME_SIGNS_LINE4("game-signs.line4"),
    GAME_FULL("messages.game-full"), IN_GAME_ALREADY("messages.already-in-game");

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
     * Get the configuration message for the specified Enum. Does account for
     * ChatColors.
     *
     * BaseComponent[] can be converted to TextComponents through the
     * TextComponent(BaseComponent[]) constructor.
     *
     * @return BaseComponent[]
     */
    public BaseComponent[] getMessage() {
        return TextComponent
                .fromLegacyText(ChatColor.translateAlternateColorCodes('&', configYAML.getString(node.toString())));
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
