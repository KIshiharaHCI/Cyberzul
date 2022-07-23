package cyberzul.network.client.messages;

/**
 * A message that informs that a user left before the game started. So he is not replaced by an AI.
 */
public class UserLeftMessage implements Message {

  private final String nickname;

  public UserLeftMessage(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }

}