package git.jluvisi.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import git.jluvisi.MicroGames;

/**
 * This class is used to manage different configuration files within the plugin.
 * More Specifically: 'config.yml' and 'signs.yml'.
 */
public class ConfigManager {

    private final MicroGames plugin;
    private final File config;
    private YamlConfiguration yamlConfig;
    private final String fileName;

    /**
     * <h3>Allows direct interaction with Configuration files.</h3> <br>
     * </br>
     * Current Configuration Files:
     * <ul>
     * <li>config.yml</li>
     * <li>signs.yml</li>
     * </ul>
     * <br>
     * </br>
     *
     *
     *
     * @param plugin
     * @param fileName
     */
    public ConfigManager(MicroGames plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.config = new File(plugin.getDataFolder(), fileName);

        if (!config.exists()) {
            plugin.getLogger().info("Config file not found... Generating.");
            plugin.saveResource(fileName, false);
        }

        this.yamlConfig = YamlConfiguration.loadConfiguration(this.config);
    }

    /**
     * Saves the configuration.
     *
     * @throws IOException
     */
    public void saveConfig() throws IOException {
        this.yamlConfig.save(config);

    }

    /**
     * Attempts to reload the yamlConfig.
     */
    public void reloadConfig() {
        this.yamlConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    /**
     * Get the configuration object.
     *
     * @return
     */
    public YamlConfiguration getConfig() {
        return this.yamlConfig;
    }

    /// METHODS TO MANIPULATE CONFIGURATION FILE ///
    public String getString(String node) {
        return this.yamlConfig.getString(node);
    }

    public boolean getBoolean(String node) {
        return this.yamlConfig.getBoolean(node);
    }

    public int getInt(String node) {
        return this.yamlConfig.getInt(node);
    }

    public List<String> getStringList(String node) {
        return this.yamlConfig.getStringList(node);
    }

    public Location getLocation(String node) {
        return this.yamlConfig.getLocation(node);
    }

    public ItemStack getItemStack(String node) {
        return this.yamlConfig.getItemStack(node);
    }

    /**
     * Set a specified value from the configuration file. This will save the config.
     *
     * @param node
     * @param value
     * @throws IOException
     */
    public void setValue(String node, Object value) throws IOException {
        this.yamlConfig.set(node, value);
        this.yamlConfig.save(config);
        // this.reloadConfig();
    }
}
