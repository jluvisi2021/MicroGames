package git.jluvisi.events;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
import git.jluvisi.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Handles when the player puts a sign down with metadata.
 */
public class SetupGameEvent implements Listener {

    private MicroGames plugin;
    private ConfigManager configYAML;

    public SetupGameEvent(MicroGames plugin) {
        this.plugin = plugin;
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    @EventHandler
    public void event(SignChangeEvent e) {
        if (!configYAML.getBoolean("game-signs.enabled")) {
            return;
        }

        // If they have recently executed the command to make a sign.
        if (e.getPlayer().hasMetadata("place-game-instance")) {
            Player p = e.getPlayer();

            // We get the individual arguments for the sign from their meta data.
            String[] args = p.getMetadata("place-game-instance").get(0).asString().split(" ");
            // Make a new game instance out of the arguments.
            GameInstance gameInstance = new GameInstance(UUID.randomUUID(), e.getBlock().getLocation(),
                    Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[3]),
                    Integer.parseInt(args[2]));
            // Add the game instance to the signleton array
            MicroGames.gameList.add(gameInstance);
            p.spigot().sendMessage(new ComponentBuilder("Game has been created.").color(ChatColor.GREEN).create());
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 5.0F, 1.0F);

            // Setup the newly placed sign.
            GameInstance.setupSignData(e, gameInstance, configYAML);

            // Remove the metadata that we set.
            p.removeMetadata("place-game-instance", plugin);
            return;
        }
    }

}
