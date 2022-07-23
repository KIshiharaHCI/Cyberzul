package cyberzul.network.client.messages;

/**
 * A chat message sent by a Player who needs help.
 */

public class PlayerNeedHelpMessage implements Message {

  public static final String CYBERZUL_HELP =
      "Cyberzul - Strategy: \n"
          + "Cyberzul is point based, which causes us to focus \n"
          + "on the amount of points we are gaining. \n"
          + "There is something inherently wrong with trying to place \n"
          + "all 5 of the same color tile on your board: it’s a terribly \n"
          + "inefficient way to get points. There’s a very simple \n"
          + "reason for choosing the blue tile right in the middle of \n"
          + "the board as the first placement: it gives you a ton \n"
          + "of options to build on for the rest of the game. \n"
          + "Blocking your opponents from moves that would net them a \n"
          + "ton of points is an absolutely devastating tactic in 2 \n"
          + "player games and very useful with more players too. \n"
          + "As with a lot of board games, focusing on the end \n"
          + "conditions is important. In this case, filling up a row \n"
          + "with tiles stops the game and determines the winner.";
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
