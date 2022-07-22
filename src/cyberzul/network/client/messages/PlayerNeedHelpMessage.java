package cyberzul.network.client.messages;

/**
 * A chat message sent by a Player who needs help.
 */

public class PlayerNeedHelpMessage extends Message {
  private final String content;

  public PlayerNeedHelpMessage(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return content;
  }

  public String getContent() {
    return content;
  }



}
