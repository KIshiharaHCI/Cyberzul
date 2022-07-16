package cyberzul.model.events;

/**
 * Gives the view the information that a player has chosen his tile. He now has to choose the row on
 * which he wants to place it.
 */
public class PlayerHasChosenTileEvent extends GameEvent {

    private final String nickname;

    public PlayerHasChosenTileEvent(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String getName() {
        return "PlayerHasChosenTileEvent";
    }

}
