package git.jluvisi.minigames;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Repersents a spigot player that can join a {@link GameInstance}. Once a
 * player joins a {@link GameInstance} they are made into a {@link GamePlayer}.
 * {@link GamePlayer}'s are normal spigot players but with additional
 * atrributes.
 */
public class GamePlayer {

    private UUID playerUUID;
    private int score;
    private boolean taskComplete;
    /**
     * Adjustable player data is data which can be given to each gameplayer and
     * managed by the game instance. Data can be used to keep track of important
     * flags that there is not specific methods for. <br>
     * </br>
     * Each time data is checked it must be parsed from an object and have an index
     * pulled from the arraylist. <br>
     * </br>
     * You can make a deep copy of this list by doing.
     * <ul>
     * <li>{@code deepCopy()}
     *
     * </ul>
     *
     *
     */
    private ArrayList<Object> adjustablePlayerData;

    /**
     * Sets up a GamePlayer. Game players are the individual players which have
     * joined a game instance.
     *
     * @param playerUUID
     * @param score
     *
     */
    public GamePlayer(UUID playerUUID, int score) {
        this.playerUUID = playerUUID;
        this.score = score;
        this.adjustablePlayerData = new ArrayList<Object>();
    }

    /**
     * Gets the UUID of a Spigot Player.
     *
     * @return playerUUID
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Gets the current score of a {@link GamePlayer}
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the current score of a {@link GamePlayer}
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns if the {@link GamePlayer} has completed a specific task.
     *
     * @return taskComplete
     */
    public boolean getTaskComplete() {
        return taskComplete;
    }

    /**
     * Identifies whether or not the player has completed a task.
     *
     * @param b
     */
    public void setTaskComplete(boolean b) {
        this.taskComplete = b;
    }

    /**
     * Get the spigot player from the {@link GamePlayer} object. The player is
     * retrieved using their {@code UUID}.
     *
     * @return Player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    /**
     * Gets the {@code adjustablePlayerData} data from the {@link GamePlayer}.
     *
     * @return List of Objects
     */
    public ArrayList<Object> getData() {
        return adjustablePlayerData;
    }

    /**
     * Returns a specific index in the players {@code adjustablePlayerData}. This
     * data is expected to return a string.
     *
     * @param index
     * @return
     */
    public String getDataString(int index) {
        return (String) this.adjustablePlayerData.get(index);
    }

    /**
     * Makes a deep copy of the arraylist.
     *
     * @return Player Data
     */
    public ArrayList<Object> deepCopy() {
        final ArrayList<Object> copy = new ArrayList<Object>();
        for (Object o : this.adjustablePlayerData) {
            copy.add(o);
        }
        return copy;
    }

    /**
     * Returns a specific index in the players {@code adjustablePlayerData}. This
     * data is expected to return a integer.
     *
     * @param index
     * @return
     */
    public int getDataInt(int index) {
        return (int) this.adjustablePlayerData.get(index);
    }

    /**
     * Returns a specific index in the players {@code adjustablePlayerData}. This
     * data is expected to return a double.
     *
     * @param index
     * @return
     */
    public double getDataDouble(int index) {
        return (double) this.adjustablePlayerData.get(index);
    }

    /**
     * Returns a specific index in the players {@code adjustablePlayerData}. This
     * data is expected to return a float.
     *
     * @param index
     * @return
     */
    public float getDataFloat(int index) {
        return (float) this.adjustablePlayerData.get(index);
    }

}
