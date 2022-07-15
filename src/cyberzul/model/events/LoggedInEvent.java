package cyberzul.model.events;

/**
 * Event that is sent by the model to the observers. It notifies about a successful login attempt
 * and that the model has adapted its state accordingly.
 */
public class LoggedInEvent extends GameEvent {

    @Override
    public String getName() {
        return "LoggedInEvent";
    }
}
