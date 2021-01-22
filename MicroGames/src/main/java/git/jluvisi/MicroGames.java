package git.jluvisi;

import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import git.jluvisi.cmds.MicrogamesCommand;
import git.jluvisi.events.DestroyGameEvent;
import git.jluvisi.events.PlayerSignJoin;
import git.jluvisi.events.SetupGameEvent;
import git.jluvisi.events.SetupSignEvent;
import git.jluvisi.minigames.GameInstance;
import git.jluvisi.minigames.GamePlayer;
import git.jluvisi.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;

/**
 *
 * <h1>MicroGames</h1> Plugin Note: Microgames is a Spigot plugin for
 * <b>MC-1.16.5</b> which has little minigames in which people on a server can
 * participate in. <br>
 * </br>
 * <b>Structure:</b> Each player can join a game which is known as a
 * {@link GameInstance}. When a player joins a {@link GameInstance} they are
 * transformed into a {@link GamePlayer}. {@link GamePlayer}s are Players but
 * with more attributes to make it easier to handle progressing through
 * minigames. When ever a player wins a game they are awarded score and
 * whichever player gets the set score for that {@link GameInstance} wins. <br>
 * </br>
 * All game instances on the server are tracked with the {@code gameList}
 * ArrayList. Retrieve the game list like so...
 *
 * <pre>
 *
 * public class SomeClass {
 *     private final MicroGames plugin;
 *
 *     public SomeClass(MicroGames plugin) {
 *         this.plugin = plugin;
 *     }
 *
 *     private void someMethod() {
 *         plugin.getGameInstances().accessMethod();
 *     }
 * }
 *
 * </pre>
 *
 * {@link GameInstance}'s can be repersented in both <i>Volatile</i> and
 * <i>Non-Volatile</i> states depending on the situation. One example of a
 * Non-Volatile {@link GameInstance} is when it is repersented through a
 * <i>Sign</i>. When a game sign is created it is stored as a
 * {@link GameInstance} but when the server is reset the {@code onDisable()}
 * method stores every game sign in memory inside of the {@code signs.yml}
 * configuration file. An example of a Volatile {@link GameInstance} is one
 * which is created through the {@code /microgames begin} command. This is a
 * temporary {@link GameInstance} that players can join through the join command
 * and play normally. However when the server is restarted this Volatile
 * {@link GameInstance} is wiped from memory.
 *
 * <br>
 * </br>
 * <a href="https://github.com/jluvisi2021/MicroGames/">All of this code is on
 * my personal GitHub.</a>
 *
 * @author Interryne/jluvisi2021
 * @version 1.0-SNAPSHOT
 * @since 1/19/2021
 *
 */
public class MicroGames extends JavaPlugin {

    /**
     * Keep track of the major configuration file version in case someone downloads
     * a newer version of the plugin with an outdated config that cannot be read
     * from correctly.
     */
    final float majorConfigVersion = 1.0F;
    /**
     * Manages the current games that are setup on the server. Games that are setup
     * in signs are loaded from the config each time the server starts.
     *
     * @see GameInstance
     */
    private final ArrayList<GameInstance> gameList = new ArrayList<GameInstance>();
    private ConfigManager configYAML;
    private ConfigManager signsYAML;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        configYAML = new ConfigManager(this, "config.yml");
        signsYAML = new ConfigManager(this, "signs.yml");
        validateConfig(configYAML);

        registerCommands();
        registerEvents();

        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        getLogger().info("Microgames has been enabled!");
        getLogger().info("Plugin Version: " + this.getDescription().getVersion());
        getLogger().info("https://github.com/jluvisi2021/MicroGames");
        getLogger().info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        GameInstance.hookJavaPlugin(this);
        setupGameInstances();

        super.onEnable();
    }

    /**
     * Retrieve the {@link GameInstance}'s that are currently being held by the
     * plugin. <br>
     * </br>
     * <strong><i>What is a {@link GameInstance}?</i></strong> <br>
     * </br>
     * Game instances are entire games that are being run or can be run that are
     * currently present on the server. Game instances which are saved as signs are
     * saved upon restart and are physically in the game. Game Instances can also be
     * made from commands such as the {@code /microgames begin} but these Game
     * Instances are not perminent and are destroyed upon reset.
     *
     * @return List of {@link GameInstance}
     */
    public ArrayList<GameInstance> getGameInstances() {
        return this.gameList;
    }

    /**
     * Pulls all of the sign data from the signs.yml configuration file and parses
     * it into the gameList arraylist.
     *
     * @see GameInstance
     * @see ConfigManager
     */
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
            GameInstance gameInstance = new GameInstance(key, instanceLocation, minPlayers, maxPlayers, startingTime,
                    winningScore);
            // Reset the text of the sign in case of server crash.
            GameInstance.updateSign(gameInstance);
            gameList.add(gameInstance);
        }
    }

    /**
     * Checks to ensure that the configuration is updated to the latest version.
     *
     * @see ConfigManager
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
     * A method that sets up the commands that this plugin has.
     *
     * @see MicrogamesCommand
     */
    private void registerCommands() {
        if (!isEnabled()) {
            return;
        }
        getCommand("microgames").setExecutor(new MicrogamesCommand(this));
    }

    /**
     * A method that sets up the events for each event this plugin has.
     *
     * @see SetupSignEvent
     * @see SetupGameEvent
     * @see DestroyGameEvent
     * @see PlayerSignJoin
     */
    private void registerEvents() {
        if (!isEnabled()) {
            return;
        }
        getServer().getPluginManager().registerEvents(new SetupSignEvent(this), this);
        getServer().getPluginManager().registerEvents(new SetupGameEvent(this), this);
        getServer().getPluginManager().registerEvents(new DestroyGameEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSignJoin(this), this);
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
        for (GameInstance gameInstance : gameList) {
            if (gameInstance.getSignLocation() == null) {
                continue;
            }
            // Reset the text of the sign in case of a server crash.
            GameInstance.updateSign(gameInstance);
            // write all the game instances to config.
            if (signsYAML.getConfig()
                    .get("minigame-signs.gameid." + gameInstance.getGameInstanceID().toString()) == null) {
                try {
                    signsYAML.setValue("minigame-signs.gameid." + gameInstance.getGameInstanceID().toString(), true);
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".location.world",
                            gameInstance.getSignLocation().getWorld().getName());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".location.x",
                            gameInstance.getSignLocation().getX());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".location.y",
                            gameInstance.getSignLocation().getY());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".location.z",
                            gameInstance.getSignLocation().getZ());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".minimum-players",
                            gameInstance.getMinPlayers());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".maximum-players",
                            gameInstance.getMaxPlayers());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".starting-time",
                            gameInstance.getStartingTime());
                    signsYAML.setValue(
                            "minigame-signs.gameid." + gameInstance.getGameInstanceID().toString() + ".winning-score",
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
