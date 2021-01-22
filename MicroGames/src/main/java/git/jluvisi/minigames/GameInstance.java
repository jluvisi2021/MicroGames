package git.jluvisi.minigames;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import git.jluvisi.MicroGames;
import git.jluvisi.events.SetupSignEvent;
import git.jluvisi.util.ConfigManager;
import git.jluvisi.util.Messages;
import net.md_5.bungee.api.ChatColor;

/**
 * Repersents a GameInstance object which can be run on the server.
 */
public class GameInstance {
    private Location signLocation;
    private ArrayList<GamePlayer> players;
    private boolean isActive;
    private int maxPlayers;
    private int minPlayers;
    private int startingTime;
    private int remaningTime;
    private int winningScore;
    private String gameID;

    /** For accessing the current class */

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
    public GameInstance(String gameID, Location signLocation, int minPlayers, int maxPlayers, int startingTime,
            int winningScore) {
        this.signLocation = signLocation;
        this.maxPlayers = maxPlayers;
        this.startingTime = startingTime;
        this.winningScore = winningScore;
        this.minPlayers = minPlayers;
        this.players = new ArrayList<GamePlayer>();
        this.isActive = false;
        this.gameID = gameID;
        this.remaningTime = startingTime;
    }

    /**
     * Set the name of the game. This name is also used in the config for storage if
     * the game is a sign.
     *
     * @param gameID
     */
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * Returns the remaning time the game has before it starts.
     *
     * @return Remaning Time
     */
    public int getRemaningTime() {
        return this.remaningTime;
    }

    /**
     * Sets the remaning time left until the game starts.
     *
     * @param i
     */
    public void setRemaningTime(int i) {
        this.remaningTime = i;
    }

    /**
     * Remove one second from remaning time.
     */
    public void remaningTimeTickDown() {
        remaningTime--;
    }

    /**
     * Reset the remaning time to default for the next game.
     */
    public void remaningTimeReset() {
        remaningTime = startingTime;
    }

    /**
     * Returns the GameID or name of the game.
     *
     * @return
     */
    public String getGameInstanceID() {
        return gameID;
    }

    /**
     * Returns a list of all of the {@link GamePlayer} objects that are currently in
     * the {@link GameInstance}.
     *
     * @return List of {@link GamePlayer}
     */
    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    /**
     * Returns whether or not the game is currently active and minigames have
     * started. A game being inside of a lobby waiting for players or the
     * {@code getRemaningTime()} > 0 repersents that the game has not officially
     * started yet.
     *
     * @return if the game is running
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the game active. This will trigger the starting of the minigames.
     *
     * @param b
     */
    public void setActive(boolean b) {
        this.isActive = b;
    }

    /**
     * Gets the winning score set when the game was created.
     *
     * @return winningScore
     */
    public int getWinningScore() {
        return winningScore;
    }

    /**
     * Gets the starting time that was set when the game was created. The starting
     * time variable is <strong>NOT</strong> modified when the game begins counting
     * down.
     *
     * @return startingTime
     */
    public int getStartingTime() {
        return startingTime;
    }

    /**
     * Returns the maximum allowed players that can be present in the lobby at once.
     * This variable is set during game creation.
     *
     * @return maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the actual location of the sign that was placed when the game was
     * created. If the game is a <i>Volatile-State</i> then this returns null.
     *
     * @see SetupSignEvent
     * @return Location Object of sign.
     */
    public Location getSignLocation() {
        return signLocation;
    }

    /**
     * Gets the minimum players required for the game to start counting down. This
     * value was set during game creation.
     *
     * @return minPlayers
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Adds a proper GamePlayer to the player list. Displays to all players who
     * joined and how many more are needed.
     *
     * @see Messages
     * @see GamePlayer
     *
     * @param gp
     */
    public void addPlayer(GamePlayer gp) {
        this.players.add(gp);

        // Send the messages from config.
        gp.getPlayer().sendMessage(
                Messages.replacePlaceholder(Messages.PLAYER_LOBBY_JOIN.getLegacyMessage(), getPlaceholders(gp)));
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getPlayer().sendMessage(Messages
                    .replacePlaceholder(Messages.GLOBAL_PLAYER_LOBBY_JOIN.getLegacyMessage(), getPlaceholders(gp)));
            if (getMinPlayers() - getPlayers().size() <= 0) {
                gamePlayer.getPlayer().sendMessage(
                        Messages.replacePlaceholder(Messages.GAME_STARTING.getLegacyMessage(), getPlaceholders(gp)));
            } else {
                gamePlayer.getPlayer().sendMessage(
                        Messages.replacePlaceholder(Messages.PLAYERS_NEEDED.getLegacyMessage(), getPlaceholders(gp)));
            }
        }
        // Update all game signs.
        updateSign(this);
    }

    /**
     * Removes a player from a game. Will update signs.
     *
     * @see GamePlayer
     * @see Messages
     *
     * @param p
     */
    public void removePlayer(GamePlayer p) {
        this.players.remove(p);
        for (GamePlayer player : players) {
            player.getPlayer().sendMessage(
                    Messages.replacePlaceholder(Messages.GLOBAL_LEAVE_GAME.getLegacyMessage(), getPlaceholders(p)));
        }
        p.getPlayer()
                .sendMessage(Messages.replacePlaceholder(Messages.LEAVE_GAME.getLegacyMessage(), getPlaceholders(p)));
        updateSign(this);
    }

    /**
     * Has all of the placeholders for the {@code replacePlaceholder} method.
     *
     * @see Messages
     * @param gp
     * @return
     */
    private final LinkedHashMap<String, String> getPlaceholders(GamePlayer gp) {
        final LinkedHashMap<String, String> placeHolderMap = new LinkedHashMap<String, String>() {

            private static final long serialVersionUID = -1719688454610734917L;

            {
                put("%player%", gp.getPlayer().getDisplayName());
                put("%time_left%", String.valueOf(getRemaningTime()));
                put("%players_needed%", String.valueOf((getMinPlayers() - getPlayers().size())));
                put("%players_in_lobby%", String.valueOf(getPlayers().size()));
                put("%max_players_allowed%", String.valueOf(getMaxPlayers()));
                put("%game_name%", getGameInstanceID());

            }
        };
        return placeHolderMap;
    }

    /**
     * Updates the join sign to reflect the player amount.
     *
     * @see MicroGames
     * @see Messages
     * @param instance
     */
    public static void updateSign(GameInstance instance) {
        if (instance.getSignLocation() == null) {
            return;
        }
        final Sign sign = ((Sign) instance.getSignLocation().getBlock().getState());
        sign.setLine(0, ChatColor.translateAlternateColorCodes('&',
                Messages.GAME_SIGNS_LINE1.getLegacyMessage() + "[MicroGames]"));
        sign.setLine(1, parseSignFromConfig(Messages.GAME_SIGNS_LINE2.getLegacyMessage(), instance));
        sign.setLine(2, parseSignFromConfig(Messages.GAME_SIGNS_LINE3.getLegacyMessage(), instance));
        sign.setLine(3, parseSignFromConfig(Messages.GAME_SIGNS_LINE4.getLegacyMessage(), instance));
        // If all of the players that can join the game are in then we can set the
        // "players full line" of the sign to the specified color.
        if (instance.getMaxPlayers() == instance.getPlayers().size()) {
            final int line = Integer.parseInt(Messages.GAME_SIGNS_GAME_COLOR_LINE.getLegacyMessage()) - 1;
            sign.setLine(line, Messages.GAME_SIGNS_GAME_FULL_COLOR.getLegacyMessage() + sign.getLine(line));
        }
        sign.update();
    }

    /**
     * Goes through all of the game instances on the server and returns a game
     * instance that has the name specified. If no game instance is found the method
     * returns null.
     *
     * @apiNote O(n)
     *
     * @param name
     * @return
     */
    public static GameInstance getByName(ArrayList<GameInstance> list, String name) {
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getGameInstanceID().equals(name)) {
                return list.get(i);
            }
        }
        return null;
    }

    /**
     * Goes through every game instance on the server and finds the game instance a
     * specific player is in.
     *
     * Returns null if no player is found.
     *
     * @apiNote O(n^2)
     *
     * @param UUID
     * @return
     */
    public static GameInstance getPlayerGame(ArrayList<GameInstance> list, UUID UUID) {
        for (GameInstance instance : list) {
            for (GamePlayer player : instance.getPlayers()) {
                if (player.getPlayerUUID() == UUID) {
                    return instance;
                }
            }
        }
        return null;
    }

    /**
     * Goes through every game instance on the server and finds the GamePlayer
     * specified. Returns null if no player is found.
     *
     * @apiNote O(n)
     *
     * @param UUID
     * @return
     */
    public static GamePlayer getPlayer(GameInstance instance, UUID uuid) {
        for (GamePlayer player : instance.getPlayers()) {
            if (player.getPlayerUUID().equals(uuid)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Checks if the class contains a player with a specified UUID.
     *
     * @see GamePlayer
     *
     * @param u
     * @return
     */
    public boolean containsPlayer(UUID u) {
        for (GamePlayer p : this.players) {
            if (p.getPlayer().getUniqueId().equals(u)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets up the data for a sign that is placed. Gets values from config using
     * {@code parseFromConfig()}
     *
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
     * <p>
     * Parses the sign data from the config.yml file to the actual sign. Replaces
     * place holders to their literal string values.
     * </p>
     * Also Parses chat colors.
     *
     * @see Messages
     *
     * @param node
     * @param instance
     * @return
     */
    private static String parseSignFromConfig(String node, GameInstance instance) {
        final LinkedHashMap<String, String> placeHolderMap = new LinkedHashMap<String, String>() {

            private static final long serialVersionUID = -5348983518483308584L;

            {
                put("%game_name%", String.valueOf(instance.getGameInstanceID()));
                put("%curr_players%", String.valueOf(instance.getPlayers().size()));
                put("%max_players%", String.valueOf(instance.getMaxPlayers()));
                put("%winning_score%", String.valueOf(instance.getWinningScore()));
                put("%min_players%", String.valueOf(instance.getMinPlayers()));
                put("%is_active%", String.valueOf(instance.isActive()));

            }
        };

        return Messages.replacePlaceholder(node, placeHolderMap);
    }

    /**
     * Returns the {@link GameInstance} as a String.
     * <ul>
     * <li><strong>Values Returned</strong></li>
     * <li>Name</li>
     * <li>World</li>
     * <li>X</li>
     * <li>Y</li>
     * <li>Z</li>
     * <li>Max Players</li>
     * <li>Min Players</li>
     * <li>Starting Time</li>
     * <li>Winning Score</li>
     * </ul>
     * <br>
     * </br>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("NAME: ").append(getGameInstanceID()).append("\n");
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
