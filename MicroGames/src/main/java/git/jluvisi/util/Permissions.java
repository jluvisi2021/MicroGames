package git.jluvisi.util;

/**
 * Manages the permissions in the plugin. Permissions are repersented as enums
 * which point to the node of the config.
 */
public enum Permissions {
    /** <strong>Node:</strong> <i>permissions.reload-config </i> */
    RELOAD_CONFIG("permissions.reload-config"),
    /** <strong>Node:</strong> <i>permissions.setup-game-sign </i> */
    SETUP_SIGN("permissions.setup-game-sign"),
    /** <strong>Node:</strong> <i>permissions.destroy-game-sign </i> */
    DESTROY_SIGN("permissions.destroy-game-sign"),
    /** <strong>Node:</strong> <i>permissions.use-join-sign </i> */
    JOIN_GAME_SIGN("permissions.use-join-sign"),
    /** <strong>Node:</strong> <i>permissions.join-game </i> */
    JOIN_GAME("permissions.join-game"),
    /** <strong>Node:</strong> <i>permissions.leave-game </i> */
    LEAVE_GAME("permissions.leave-game"),
    /** <strong>Node:</strong> <i>permissions.leave-game </i> */
    BEGIN_GAME("permissions.start-game"),
    /** <strong>Node:</strong> <i>notify-announce-game-start </i> */
    NOTIFY("permissions.notify-announce-game-start");

    /** Repersents the path in the config relative to the enum. */
    private final String node;

    Permissions(String node) {
        this.node = node;
    }

    /** Returns the raw node of the config without any parsing. */
    @Override
    public String toString() {
        return node;
    }
}
