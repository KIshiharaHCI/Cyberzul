package cyberzul.model.events;

import cyberzul.network.client.messages.Message;

/**
 * Informs the listeners that a player was successfully added to the game.
 */
public class PlayerAddedEvent extends GameEvent {

  public final String nickname;

  public PlayerAddedEvent(String nickname) {
    this.nickname = nickname;
  }
  public String getNickname() {
    return nickname;
  }
  public String getName() {
    return "PlayerAddedEvent";
  }
}
