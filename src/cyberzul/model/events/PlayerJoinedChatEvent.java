package cyberzul.model.events;

import cyberzul.network.client.messages.Message;

/**
 * Event which is sent by the model to the observers.
 * It notifies about a new message that a new participating player
 * joins in the chat when the player joins the game.
 */


public class PlayerJoinedChatEvent extends GameEvent {
    private final Message message;

    public PlayerJoinedChatEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String getName() {
        return "PlayerJoinedChatEvent";
    }

}
