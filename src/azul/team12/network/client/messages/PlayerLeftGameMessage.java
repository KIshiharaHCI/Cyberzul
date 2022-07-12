package azul.team12.network.client.messages;

/**
 * A system message for the chat containing the data of a user that has left the Game.
 */

public class PlayerLeftGameMessage extends Message {
    private final String nickname;

    public PlayerLeftGameMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
