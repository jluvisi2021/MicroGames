package git.jluvisi.events;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Permissions;
import git.jluvisi.util.SpigotHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Handles when the player destroys a game sign. What we check:
 * <ul>
 * <li>If the Sign Material in Config is valid.</li>
 * <li>If the block broken is a sign.</li>
 * <li>If its first line has {@code [MicroGames]} with Config line 1
 * formatting.</li>
 * <li>If they have permission to destroy the sign.</li>
 * <li>If the location of the sign is in the {@code plugin.getGameInstances()}
 * list.
 * </ul>
 */
public class DestroyGameEvent implements Listener {

    private final ConfigManager configYAML;
    private final MicroGames plugin;

    public DestroyGameEvent(MicroGames plugin) {
        this.plugin = plugin;
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    Material signMaterial = Material.OAK_SIGN;
    Material signMaterialWall = Material.OAK_WALL_SIGN;

    @EventHandler
    public void event(BlockBreakEvent e) {
        final Player p = e.getPlayer();
        // if the material from the config is valid.
        if (SpigotHelper.isValidMaterial(configYAML.getString("game-signs.sign-material") + "_SIGN")) {
            signMaterial = Material.getMaterial(configYAML.getString("game-signs.sign-material") + "_SIGN");
            signMaterialWall = Material.getMaterial(configYAML.getString("game-signs.sign-material") + "_WALL_SIGN");
        }
        // if the block broken is a sign
        if (!Tag.SIGNS.isTagged(e.getBlock().getType())) {
            return;
        }
        final Sign sign = (Sign) e.getBlock().getState();
        // If the first line is [MicroGames]
        if (sign.getLine(0).equals(ChatColor.translateAlternateColorCodes('&',
                configYAML.getString("game-signs.line1-color") + "[MicroGames]"))) {
            // If the player is allowed to destroy minigame signs.
            if (!p.hasPermission(Permissions.DESTROY_SIGN.toString())) {
                TextComponent message = new TextComponent("You do not have permission to destroy game signs!");
                message.setColor(ChatColor.RED);
                p.spigot().sendMessage(message);
                e.setCancelled(true);
                return;
            }

            // Then we remove the sign from the arraylist.
            final int size = plugin.getGameInstances().size();
            for (int i = 0; i < size; i++) {
                if (plugin.getGameInstances().get(i).getSignLocation().getWorld()
                        .equals(sign.getLocation().getWorld())) {

                    // Since blocks do not need precise comparison (Almost always whole numbers) we
                    // can just use "=="
                    if (plugin.getGameInstances().get(i).getSignLocation().getX() == sign.getLocation().getX()
                            && plugin.getGameInstances().get(i).getSignLocation().getY() == sign.getLocation().getY()
                            && plugin.getGameInstances().get(i).getSignLocation().getZ() == sign.getLocation().getZ()) {
                        plugin.getGameInstances().remove(i);
                        p.sendMessage(ChatColor.RED + "Destroyed Game Sign.");
                    }

                }
            }

        }

    }

}
