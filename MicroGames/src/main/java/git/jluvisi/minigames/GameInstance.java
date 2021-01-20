package git.jluvisi.minigames;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.block.SignChangeEvent;

import git.jluvisi.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;

public class GameInstance {

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
     * Sets up the data for a sign that is placed.
     *
     * @param e
     * @param instance
     */
    public static void setupSignData(SignChangeEvent e, GameInstance instance, ConfigManager configYAML) {
        e.setLine(0, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "[MicroGames]");
        e.setLine(1, parseSignFromConfig(configYAML.getString("game-signs.line2"), instance));
        e.setLine(2, parseSignFromConfig(configYAML.getString("game-signs.line3"), instance));
        e.setLine(3, parseSignFromConfig(configYAML.getString("game-signs.line4"), instance));
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
        node = node.replace("%curr_players%", "0");
        node = node.replace("%max_players%", String.valueOf(instance.getMaxPlayers()));
        node = node.replace("%winning_score%", String.valueOf(instance.getWinningScore()));
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
