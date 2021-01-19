package git.jluvisi;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.cmds.MicrogamesCommand;
import git.jluvisi.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;

/**
 *
 * Plugin Note: Microgames is a Spigot plugin for 1.16.5 which has little
 * minigames in which people on a server can participate in!
 *
 * @author Interryne/jluvisi2021
 * @version 1.0-SNAPSHOT
 *
 */
public class MicroGames extends JavaPlugin {

    final float majorConfigVersion = 1.0F;

    @Override
    public void onEnable() {
        loadConfig();
        registerCommands();
        registerEvents();

        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("Microgames has been enabled!");
        getLogger().info("Plugin Version: " + this.getDescription().getVersion());
        getLogger().info("https://github.com/jluvisi2021/MicroGames");
        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        super.onEnable();
    }

    /**
     * Checks to ensure that the configuration is updated to the latest version.
     *
     * @param configManager
     */
    private void validateConfig(ConfigManager configManager) {
        try {
            if (Float.parseFloat(configManager.getString("major-config-version")) != majorConfigVersion) {
                getLogger().severe(ChatColor.RED
                        + "ERROR: CONFIGURATION FILE IS OUTDATED OR WRONG. PLEASE VIEW THE WEBSITE FOR MORE INFORMATION ON HOW TO REGENERATE THE CONFIG FILE.");
                getServer().getPluginManager().disablePlugin(this);
            }
        } catch (NumberFormatException e) {
            getLogger().severe(ChatColor.RED
                    + "ERROR: CONFIGURATION FILE IS OUTDATED OR WRONG. PLEASE VIEW THE WEBSITE FOR MORE INFORMATION ON HOW TO REGENERATE THE CONFIG FILE.");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    /**
     * Loads the config.yml and data.yml files.
     */
    private void loadConfig() {
        ConfigManager configYAML = new ConfigManager(this, "config.yml");
        ConfigManager dataYAML = new ConfigManager(this, "data.yml");
        try {
            configYAML.saveConfig();
            configYAML.reloadConfig();
            dataYAML.saveConfig();
            dataYAML.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        validateConfig(configYAML);
    }

    /**
     * A method that sets up the commands that this plugin has.
     */
    private void registerCommands() {
        getCommand("microgames").setExecutor(new MicrogamesCommand(this));
    }

    /**
     * A method that sets up the events for each event this plugin has.
     */
    private void registerEvents() {

    }

    @Override
    public void onDisable() {

        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("Microgames has been disabled!");
        getLogger().info("Plugin Version: " + this.getDescription().getVersion());
        getLogger().info("https://github.com/jluvisi2021/MicroGames");
        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        super.onDisable();
    }

}
