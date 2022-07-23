package cyberzul.network.client.messages;

/**
 * A system message for the chat. It contains the data that the user of this client has It contains
 * the data that the user of this client has successfully logged in.
 */
public class PlayerLoggedInMessage implements Message {
  private final String nickname;

  public PlayerLoggedInMessage(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }
}
