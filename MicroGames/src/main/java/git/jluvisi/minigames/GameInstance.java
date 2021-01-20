package git.jluvisi.minigames;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Messages;
import net.md_5.bungee.api.ChatColor;

public class GameInstance {

    private static MicroGames plugin;
    private Location signLocation;
    private ArrayList<GamePlayer> players;
    private boolean isActive;
    private int maxPlayers;
    private int minPlayers;
    private int startingTime;
    private int winningScore;
    private UUID gameID;

    /**
     * Handles the game instance object. Game instances are instances of games that
     * can be run or are running.
     *
     * @param gameID
     * @param signLocation
     * @param minPlayers
     * @param maxPlayers
     * @param startingTime
     * @param winningScore
     */
    public GameInstance(UUID gameID, Location signLocation, int minPlayers, int maxPlayers, int startingTime,
            int winningScore) {
        this.signLocation = signLocation;
        this.maxPlayers = maxPlayers;
        this.startingTime = startingTime;
        this.winningScore = winningScore;
        this.minPlayers = minPlayers;
        players = new ArrayList<GamePlayer>();
        isActive = false;
        this.gameID = gameID;

    }

    /**
     * Hooks the Main Class to this plugin. Although this is not a great
     * implementation I dont want to dependency inject each time we need to make a
     * game instance object. On top of that the JavaPlugin is singleton so we can do
     * this safely.
     *
     * @param plugin
     */
    public static final void hookJavaPlugin(MicroGames plugin) {
        GameInstance.plugin = plugin;
    }

    public void generateGameUUID() {
        gameID = UUID.randomUUID();
    }

    public UUID getGameInstanceUUID() {
        return gameID;
    }

    public void setGameInstanceUUID(String u) {
        this.gameID = UUID.fromString(u);
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean b) {
        this.isActive = b;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Adds a proper GamePlayer to the player list. Displays to all players who
     * joined and how many more are needed.
     *
     * @param gp
     * @param signJoin (If the player joined through a sign)
     */
    public void addPlayer(GamePlayer gp, boolean signJoin) {
        this.players.add(gp);
        int playersNeeded = (getMinPlayers() - getPlayers().size());
        gp.getPlayer()
                .sendMessage(ChatColor.GRAY + "Joined Lobby: " + ChatColor.GREEN + MicroGames.gameList.indexOf(this));
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getPlayer().sendMessage(ChatColor.GREEN + gp.getPlayer().getDisplayName() + ChatColor.GRAY
                    + " has joined the lobby! " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + getPlayers().size()
                    + ChatColor.GRAY + "/" + ChatColor.GOLD + getMaxPlayers() + ChatColor.DARK_GRAY + ")");

            if (playersNeeded <= 0) {
                gamePlayer.getPlayer().sendMessage(
                        ChatColor.GRAY + "Game starting in: " + ChatColor.GREEN + getStartingTime() + " seconds!");
            } else {
                gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + String.valueOf(playersNeeded) + " players "
                        + ChatColor.GRAY + "needed to start the game.");
            }
        }
        if (!signJoin) {
            return;
        }
        updateSign(this);
    }

    /**
     * Removes a player from a game.
     *
     * @param p
     */
    public void removePlayer(GamePlayer p) {
        this.players.remove(p);
        updateSign(this);
    }

    /**
     * Updates the join sign to reflect the player amount.
     *
     * @apiNote Must hook into the main class before executed.
     *
     * @see MicroGames
     * @see Messages
     * @param instance
     */
    public static void updateSign(GameInstance instance) {
        if (instance.getSignLocation() == null) {
            return;
        }
        Sign sign = ((Sign) instance.getSignLocation().getBlock().getState());
        sign.setLine(0, ChatColor.translateAlternateColorCodes('&',
                Messages.GAME_SIGNS_LINE1.getLegacyMessage() + "[MicroGames]"));
        sign.setLine(1, parseSignFromConfig(Messages.GAME_SIGNS_LINE2.getLegacyMessage(), instance));
        sign.setLine(2, parseSignFromConfig(Messages.GAME_SIGNS_LINE3.getLegacyMessage(), instance));
        sign.setLine(3, parseSignFromConfig(Messages.GAME_SIGNS_LINE4.getLegacyMessage(), instance));
        if (instance.getMaxPlayers() == instance.getPlayers().size()) {
            ConfigManager configYML = new ConfigManager(plugin, "config.yml");
            final int line = configYML.getInt("game-signs.game-full-color-line") - 1;
            sign.setLine(line, ChatColor.translateAlternateColorCodes('&',
                    configYML.getString("game-signs.game-full-color") + sign.getLine(line)));
        }
        sign.update();
    }

    /**
     * Sets up the data for a sign that is placed.
     *
     * @param e
     * @param instance
     */
    public static void setupSignData(SignChangeEvent e, GameInstance instance, ConfigManager configYAML) {
        e.setLine(0, ChatColor.translateAlternateColorCodes('&',
                Messages.GAME_SIGNS_LINE1.getLegacyMessage() + "[MicroGames]"));
        e.setLine(1, parseSignFromConfig(Messages.GAME_SIGNS_LINE2.getLegacyMessage(), instance));
        e.setLine(2, parseSignFromConfig(Messages.GAME_SIGNS_LINE3.getLegacyMessage(), instance));
        e.setLine(3, parseSignFromConfig(Messages.GAME_SIGNS_LINE4.getLegacyMessage(), instance));
    }

    /**
     * Parses the sign data from the config.yml file to the actual sign.
     *
     * @param node
     * @param instance
     * @return
     */
    private static String parseSignFromConfig(String node, GameInstance instance) {
        node = ChatColor.translateAlternateColorCodes('&', node);
        node = node.replace("%curr_players%", String.valueOf(instance.getPlayers().size()));
        node = node.replace("%max_players%", String.valueOf(instance.getMaxPlayers()));
        node = node.replace("%winning_score%", String.valueOf(instance.getWinningScore()));
        node = node.replace("%min_players%", String.valueOf(instance.getMinPlayers()));
        node = node.replace("%is_active%", String.valueOf(instance.isActive()));
        return node;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("UID: ").append(getGameInstanceUUID()).append("\n");
        str.append("WORLD: ").append(getSignLocation().getWorld().getName()).append("\n");
        str.append("X: ").append(getSignLocation().getX()).append("\n");
        str.append("Y: ").append(getSignLocation().getY()).append("\n");
        str.append("Z: ").append(getSignLocation().getZ()).append("\n");
        str.append("Max P: ").append(maxPlayers).append("\n");
        str.append("Min P: ").append(minPlayers).append("\n");
        str.append("Starting Time: ").append(startingTime).append("\n");
        str.append("Winning Score: ").append(winningScore).append("\n");
        return str.toString();
    }

}
