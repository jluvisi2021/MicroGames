package git.jluvisi.util;

/**
 * Manages the permissions in the plugin. Permissions are repersented as enums
 * which point to the node of the config.
 */
public enum Permissions {

    RELOAD_CONFIG("permissions.reload-config"), SETUP_SIGN("permissions.setup-game-sign"),
    DESTROY_SIGN("permissions.destroy-game-sign"), JOIN_GAME("permissions.use-join-sign");

    private String node;

    Permissions(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return node;
    }
}
