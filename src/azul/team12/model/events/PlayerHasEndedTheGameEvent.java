package azul.team12.model.events;

/**
 * Informs the listeners that a player has ended the game. Also carries the information of the
 * player who has ended the game.
 */

public class PlayerHasEndedTheGameEvent extends GameEvent {

    public final String ENDER;

    public PlayerHasEndedTheGameEvent(String nickname){
      this.ENDER = nickname;
    }

    public String getENDER() {
      return ENDER;
    }

    @Override
    public String getName(){
      return "PlayerHasEndedTheGameEvent";
    }

}
