package cyberzul.network.client.messages;


/**
 * A system messages that shows who is the next player to draw the tile.
 */
public class NextPlayersTurnMessage extends Message {

    private final String nickname;

    public NextPlayersTurnMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
