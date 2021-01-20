package git.jluvisi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.cmds.MicrogamesCommand;
import git.jluvisi.events.DestroyGameEvent;
import git.jluvisi.events.SetupGameEvent;
import git.jluvisi.events.SetupSignEvent;
import git.jluvisi.minigames.GameInstance;
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

    /**
     * Keep track of the major configuration file version in case someone downloads
     * a newer version of the plugin with an outdated config that cannot be read
     * from correctly.
     */
    final float majorConfigVersion = 1.0F;
    public static ArrayList<GameInstance> gameList = new ArrayList<GameInstance>();
    private ConfigManager configYAML;
    private ConfigManager signsYAML;

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

        setupGameInstances();
        super.onEnable();
    }

    private void setupGameInstances() {
        ConfigurationSection minigameSection = signsYAML.getConfig().getConfigurationSection("minigame-signs.gameid");
        if (minigameSection == null) {
            return;
        }
        for (String key : minigameSection.getKeys(false)) {
            Location instanceLocation = new Location(
                    getServer().getWorld(signsYAML.getString("minigame-signs.gameid." + key + ".location.world")),
                    Double.parseDouble(signsYAML.getString("minigame-signs.gameid." + key + ".location.x")),
                    Double.parseDouble(signsYAML.getString("minigame-signs.gameid." + key + ".location.y")),
                    Double.parseDouble(signsYAML.getString("minigame-signs.gameid." + key + ".location.z")));
            int minPlayers = Integer.parseInt(signsYAML.getString("minigame-signs.gameid." + key + ".minimum-players"));
            int maxPlayers = Integer.parseInt(signsYAML.getString("minigame-signs.gameid." + key + ".maximum-players"));
            int startingTime = Integer.parseInt(signsYAML.getString("minigame-signs.gameid." + key + ".starting-time"));
            int winningScore = Integer.parseInt(signsYAML.getString("minigame-signs.gameid." + key + ".winning-score"));
            GameInstance gameInstance = new GameInstance(UUID.fromString(key), instanceLocation, minPlayers, maxPlayers,
                    startingTime, winningScore);
            gameList.add(gameInstance);
        }
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
     * Loads the config.yml and signs.yml files.
     */
    private void loadConfig() {
        configYAML = new ConfigManager(this, "config.yml");
        signsYAML = new ConfigManager(this, "signs.yml");
        try {
            configYAML.saveConfig();
            configYAML.reloadConfig();
            signsYAML.saveConfig();
            signsYAML.reloadConfig();
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
        getServer().getPluginManager().registerEvents(new SetupSignEvent(this), this);
        getServer().getPluginManager().registerEvents(new SetupGameEvent(this), this);
        getServer().getPluginManager().registerEvents(new DestroyGameEvent(this), this);
    }

    @Override
    public void onDisable() {

        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("Microgames has been disabled!");
        getLogger().info("Plugin Version: " + this.getDescription().getVersion());
        getLogger().info("https://github.com/jluvisi2021/MicroGames");
        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        // Delete the entire signsYAML file to replace it with everything in the list.
        try {
            signsYAML.setValue("minigame-signs.gameid", null);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Check to see if any new game instances have been made since last restart.
        for (GameInstance gameInstance : MicroGames.gameList) {
            if (gameInstance.getGameInstanceUUID() == null) {
                gameInstance.generateGameUUID();
            }
            // write all the game instances to config.
            if (signsYAML.getConfig()
                    .get("minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString()) == null) {
                try {
                    signsYAML.setValue("minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString(), true);
                    signsYAML.setValue("minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString()
                            + ".location.world", gameInstance.getSignLocation().getWorld().getName());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString() + ".location.x",
                            gameInstance.getSignLocation().getX());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString() + ".location.y",
                            gameInstance.getSignLocation().getY());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString() + ".location.z",
                            gameInstance.getSignLocation().getZ());
                    signsYAML.setValue("minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString()
                            + ".minimum-players", gameInstance.getMinPlayers());
                    signsYAML.setValue("minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString()
                            + ".maximum-players", gameInstance.getMaxPlayers());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString() + ".starting-time",
                            gameInstance.getStartingTime());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceUUID().toString() + ".winning-score",
                            gameInstance.getWinningScore());
                    signsYAML.saveConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        super.onDisable();
    }

}
