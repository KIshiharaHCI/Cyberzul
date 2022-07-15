package cyberzul.network.client.messages;

/**
 * A system message for the chat.
 * It contains the data of a user that has joined the chat.
 */


public class PlayerJoinedChatMessage extends Message {
  private final String nickname;

  public PlayerJoinedChatMessage(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }


}
