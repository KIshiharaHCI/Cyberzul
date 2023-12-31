package cyberzul.network.client.messages;

/**
 * A system message for the chat containing the data of a user that has left the Game.
 */
public class PlayerForfeitedMessage implements Message {
  private final String nickname;

  public PlayerForfeitedMessage(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }
}
