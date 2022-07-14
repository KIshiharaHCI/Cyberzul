package azul.team12.model.events;

import azul.team12.network.client.messages.Message;

/**
 * Event which is sent by the model to the observers. It notifies about a new message that has been
 * received from another participating player in the chat.
 */


public class PlayerAddedMessageEvent extends GameEvent {
    private final Message message;

    public PlayerAddedMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String getName() {
        return "PlayerAddedMessageEvent";
    }

}
