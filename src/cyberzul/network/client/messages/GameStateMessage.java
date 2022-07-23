package cyberzul.network.client.messages;

/**
 * A message from the server, informing the player of changes.
 */
public class GameStateMessage implements Message {

  public String content;

  public GameStateMessage(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
