package git.jluvisi.events;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Handles when a player has destroyed a game sign.
 */
public class DestroyGameEvent implements Listener {

    private ConfigManager configYAML;

    public DestroyGameEvent(MicroGames plugin) {
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    @EventHandler
    public void event(BlockBreakEvent e) {
        if (!configYAML.getBoolean("game-signs.enabled")) {
            return;
        }
        Player p = e.getPlayer();
        // if the block broken is a sign
        if (e.getBlock().getType() == Material.OAK_SIGN) {
            Sign sign = (Sign) e.getBlock().getState();
            // If the first line is [MicroGames]
            if (sign.getLine(0).equals(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "[MicroGames]")) {
                // If the player is allowed to destroy minigame signs.
                if (!p.hasPermission(Permissions.DESTROY_SIGN.toString())) {
                    TextComponent message = new TextComponent("You do not have permission to destroy game signs!");
                    message.setColor(ChatColor.RED);
                    e.getPlayer().spigot().sendMessage(message);
                    e.setCancelled(true);
                    return;
                }

                // Then we remove the sign from the arraylist.
                final int size = MicroGames.gameList.size();
                for (int i = 0; i < size; i++) {
                    if (MicroGames.gameList.get(i).getSignLocation().equals(sign.getLocation())) {
                        p.spigot().sendMessage(new TextComponent("Destroyed Game Sign."));
                        MicroGames.gameList.remove(i);
                    }
                }

            }
        }

    }

}
