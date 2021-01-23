package git.jluvisi.minigames;

/**
 * Repersents the current state a {@link GameInstance} is currently in. <br>
 * </br>
 * <strong>Game States:</strong>
 * <table style="width:100%">
 * <tr>
 * <th>Game State</th>
 * <th>Can Join</th>
 * <th>Is Running</th>
 * </tr>
 * <tr>
 * <td>IN_LOBBY</td>
 * <td>Yes</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>IN_PROGRESS</td>
 * <td>No</td>
 * <td>Yes</td>
 * </tr>
 * <td>DISABLED</td>
 * <td>No</td>
 * <td>No</td>
 * <tr>
 * </tr>
 * </table>
 */
public enum GameState {
    IN_LOBBY, IN_PROGRESS, DISABLED
}
