package git.jluvisi.minigames;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GamePlayer {

    private UUID playerUUID;
    private int score;
    private boolean taskComplete;
    private ArrayList<Object> adjustablePlayerData;

    /**
     * Sets up a GamePlayer. Game players are the individual players which have
     * joined a game instance.
     *
     * @param playerUUID
     * @param score
     */
    public GamePlayer(UUID playerUUID, int score) {
        this.playerUUID = playerUUID;
        this.score = score;
        this.adjustablePlayerData = new ArrayList<Object>();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(boolean b) {
        this.taskComplete = b;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public ArrayList<Object> getData() {
        return adjustablePlayerData;
    }

}
