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
 * More Specifically: 'config.yml' and 'data.yml'.
 */
public class ConfigManager {
    private MicroGames plugin;
    private File config;
    private YamlConfiguration yamlConfig;
    private String fileName;

    /**
     * Setup the constructor for one individual configuration file.
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
        this.getConfig().save(fileName);
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
     * Set a specified value from the configuration file. This will both SAVE and
     * RELOAD the configuration.
     *
     * @param node
     * @param value
     * @throws IOException
     */
    public void setValue(String node, Object value) throws IOException {
        this.yamlConfig.set(node, value);
        this.saveConfig();
        this.reloadConfig();
    }
}