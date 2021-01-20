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

    private final ConfigManager configYAML;

    public DestroyGameEvent(final MicroGames plugin) {
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    @EventHandler
    public void event(BlockBreakEvent e) {
        Player p = e.getPlayer();
        // if the block broken is a sign
        if (e.getBlock().getType() == Material.OAK_SIGN) {
            Sign sign = (Sign) e.getBlock().getState();
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
                final int size = MicroGames.gameList.size();
                for (int i = 0; i < size; i++) {
                    if (MicroGames.gameList.get(i).getSignLocation().equals(sign.getLocation())) {
                        TextComponent message = new TextComponent("Destroyed Game Sign.");
                        message.setColor(ChatColor.RED);
                        p.spigot().sendMessage(message);
                        MicroGames.gameList.remove(i);
                        return;
                    }
                }

            }
        }

    }

}
