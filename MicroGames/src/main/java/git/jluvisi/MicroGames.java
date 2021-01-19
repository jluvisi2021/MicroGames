package git.jluvisi;

import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
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
     * A method that sets up the commands that this plugin has.
     */
    private void registerCommands() {

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
