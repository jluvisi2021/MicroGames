package git.jluvisi.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
import git.jluvisi.minigames.GamePlayer;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Messages;
import git.jluvisi.util.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerSignJoin implements Listener {

    private final ConfigManager configYAML;
    private final MicroGames plugin;

    public PlayerSignJoin(final MicroGames plugin) {
        this.plugin = plugin;
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        // if the player right clicked a block.
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // It is a sign
        if (e.getClickedBlock().getType() != Material.OAK_WALL_SIGN
                && e.getClickedBlock().getType() != Material.OAK_SIGN) {
            return;
        }

        Player p = e.getPlayer();

        // Signs are enabled.
        if (!configYAML.getBoolean("game-signs.enabled")) {
            p.spigot().sendMessage(new TextComponent(Messages.GAME_SIGNS_DISABLED.getMessage()));
            return;
        }

        Sign sign = (Sign) e.getClickedBlock().getState();
        // Has [MicroGames]
        if (!sign.getLine(0).equals(ChatColor.translateAlternateColorCodes('&',
                configYAML.getString("game-signs.line1-color") + "[MicroGames]"))) {
            return;
        }
        final int size = MicroGames.gameList.size();
        int index = -1;
        GameInstance instance = null;
        for (int i = 0; i < size; i++) {
            // The instance is a real game instance.
            if (MicroGames.gameList.get(i).getSignLocation().equals(sign.getLocation())) {
                instance = MicroGames.gameList.get(i);
                index = i;
                break;
            }
        }
        // if the instance can be joined.
        if (instance.getPlayers().size() >= instance.getMaxPlayers()) {
            p.spigot().sendMessage(new TextComponent(Messages.GAME_FULL.getMessage()));
            return;
        }
        // The player has the permission.
        if (!e.getPlayer().hasPermission(Permissions.JOIN_GAME.toString())) {
            p.spigot().sendMessage(new TextComponent(Messages.NO_PERMISSION.getMessage()));
            return;
        }

        GamePlayer gp = new GamePlayer(e.getPlayer().getUniqueId(), 0);
        for (GamePlayer g : instance.getPlayers()) {
            if (g.getPlayerUUID() == p.getUniqueId()) {
                p.spigot().sendMessage(new TextComponent(Messages.IN_GAME_ALREADY.getMessage()));
                return;
            }
        }
        // Add the player to the game instance.
        MicroGames.gameList.get(index).addPlayer(gp, true);
    }

}
