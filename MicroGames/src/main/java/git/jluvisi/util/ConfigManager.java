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
 * More Specifically: 'config.yml' and 'signs.yml'. <br>
 * </br>
 * The Config File can be accessed through direct-access or indirect-access.
 * Indirect access is usually reccomended because the methods can be changed
 * over time to provide for more support while the spigot methods are out of my
 * control. <br>
 * </br>
 * <h3>Indirect Access Methods</h3>
 * <ul>
 * <li>{@code saveConfig()}</li>
 * <li>{@code reloadConfig()}</li>
 * <li>{@code getString(node)}</li>
 * <li>{@code getBoolean(node)}</li>
 * <li>{@code getInt(node)}</li>
 * <li>{@code getStringList(node)}</li>
 * <li>{@code getLocation(node)}</li>
 * <li>{@code getItemStack(node)}</li>
 * <li>{@code setValue(node, value)}</li>
 * </ul>
 */
public class ConfigManager {

    /** Main Class */
    private final MicroGames plugin;
    /** The config file repersented by a Java {@code File} object. */
    private final File config;
    /** The actual config file. */
    private YamlConfiguration yamlConfig;
    /** Name of the file. */
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

    /**
     * Returns the value of the config at a specific node.
     *
     * @param node
     * @return
     */
    public String getString(String node) {
        return this.yamlConfig.getString(node);
    }

    /**
     * Returns the value of the config at a specific node.
     *
     * @param node
     * @return
     */
    public boolean getBoolean(String node) {
        return this.yamlConfig.getBoolean(node);
    }

    /**
     * Returns the value of the config at a specific node.
     *
     * @param node
     * @return
     */
    public int getInt(String node) {
        return this.yamlConfig.getInt(node);
    }

    /**
     * Returns the value of the config at a specific node.
     *
     * @param node
     * @return
     */
    public List<String> getStringList(String node) {
        return this.yamlConfig.getStringList(node);
    }

    /**
     * Returns the value of the config at a specific node.
     *
     * @param node
     * @return org.bukkit.Location
     */
    public Location getLocation(String node) {
        return this.yamlConfig.getLocation(node);
    }

    /**
     * Returns the value of the config at a specific node.
     *
     * @return org.bukkit.inventory.ItemStack
     */
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
