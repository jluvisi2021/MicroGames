package git.jluvisi.events;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import git.jluvisi.MicroGames;
import git.jluvisi.minigames.GameInstance;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.SpigotHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Handles when the user enters their arguments in chat to setup a sign.
 */
public class SetupSignEvent implements Listener {

    private final MicroGames plugin;
    private final ConfigManager configYAML;

    public SetupSignEvent(final MicroGames plugin) {
        this.plugin = plugin;
        this.configYAML = new ConfigManager(plugin, "config.yml");
    }

    /** Material of the sign. By default it is OAK. */
    private Material signMaterial = Material.OAK_SIGN;

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        configYAML.reloadConfig();
        if (!configYAML.getBoolean("game-signs.enabled")) {
            return;
        }
        // if they have the metadata and have recently executed the command to setup a
        // sign.
        if (e.getPlayer().hasMetadata("setup-begin")) {
            final String message = e.getMessage();
            final Player p = e.getPlayer();

            final String[] args = message.split(" ");

            int numMinPlayers;
            int numMaxPlayers;
            int winningScore;
            int gameStartTime;

            // Make sure all the arguments are numbers.
            try {
                numMinPlayers = Integer.parseInt(args[0]);
                numMaxPlayers = Integer.parseInt(args[1]);
                winningScore = Integer.parseInt(args[2]);
                gameStartTime = Integer.parseInt(args[3]);

                // They follow the correct criteria
                if (!(GameInstance.minPlayersRange.contains(numMinPlayers)
                        && GameInstance.maxPlayersRange.contains(numMaxPlayers)
                        && GameInstance.winningScoreRange.contains(winningScore)
                        && GameInstance.startingTimeRange.contains(gameStartTime))) {
                    throw new NumberFormatException();
                }

            } catch (final NumberFormatException exception) {
                p.spigot().sendMessage(new ComponentBuilder().color(ChatColor.RED).append(
                        "One or more of your arguments to identify this game were incorrect. Please repeat the command and try again.")
                        .create());
                p.removeMetadata("setup-begin", plugin);
                p.removeMetadata("setup-game-name", plugin);
                e.setCancelled(true);
                return;
            }

            // if the material from the config is valid.
            if (SpigotHelper.isValidMaterial(configYAML.getString("game-signs.sign-material") + "_SIGN")) {
                signMaterial = Material.getMaterial(configYAML.getString("game-signs.sign-material") + "_SIGN");
            }

            // Setup the sign with the arguments
            final ItemStack sign = SpigotHelper.compactItem(signMaterial, ChatColor.YELLOW + "Game Sign", 1,
                    Arrays.asList(
                            ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Place this sign to activate it.",
                            ChatColor.GOLD + "Game Instance Settings:",
                            ChatColor.GRAY + "Game Name: " + ChatColor.GREEN
                                    + p.getMetadata("setup-game-name").get(0).asString(),
                            ChatColor.GRAY + "Minimum Players: " + ChatColor.GREEN + numMinPlayers,
                            ChatColor.GRAY + "Maximum Players: " + ChatColor.GREEN + numMaxPlayers,
                            ChatColor.GRAY + "Winning Score: " + ChatColor.GREEN + winningScore,
                            ChatColor.GRAY + "Game Start Time: " + ChatColor.GREEN + gameStartTime + "s"));
            p.getInventory().getItemInMainHand().setType(Material.AIR);
            p.getInventory().addItem(sign);
            p.removeMetadata("setup-begin", plugin);
            // Make it so the user now can place down a sign.
            p.setMetadata("place-game-instance", new FixedMetadataValue(plugin, e.getMessage()));
            p.spigot()
                    .sendMessage(new ComponentBuilder().color(ChatColor.GRAY).append("Place the \"")
                            .color(ChatColor.GRAY).append("Game Sign").color(ChatColor.YELLOW)
                            .append("\" and then press \"Done\".").color(ChatColor.GRAY).create());
            e.setCancelled(true);
        }
    }

}
