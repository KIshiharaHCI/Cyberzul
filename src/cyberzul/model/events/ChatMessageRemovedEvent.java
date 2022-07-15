package cyberzul.model.events;


import cyberzul.network.client.messages.Message;

/**
 * Event that is sent by the model to the observers.
 * It notifies about a chat message that has been removed from the collection of stored messages.
 */


public class ChatMessageRemovedEvent extends GameEvent {
  private final Message message;

  public ChatMessageRemovedEvent(Message message) {
    this.message = message;
  }

  public Message getMessage() {
    return message;
  }

  @Override
  public String getName() {
    return "ChatMessageRemovedEvent";
  }


}
