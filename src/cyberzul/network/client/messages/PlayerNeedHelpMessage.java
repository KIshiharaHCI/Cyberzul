package cyberzul.network.client.messages;

/**
 * A chat message sent by a Player who needs help.
 */

public class PlayerNeedHelpMessage implements Message {

  public static final String CYBERZUL_HELP =
      "Cyberzul - Strategy: \n"
          + "Azul is point based, which causes us to focus \n"
          + "on the amount of points we are gaining. \n"
          + "There is something inherently wrong with trying \n"
          + "to place all 5 of the same color tile on your board: \n"
          + "it’s a terribly inefficient way to get points. \n"
          + "There’s a very simple reason for choosing the blue \n"
          + "tile right in the middle of the board as the first \n"
          + "placement: it gives you a ton of options to build on \n"
          + "for the rest of the game. Blocking your opponents from \n"
          + "moves that would net them a ton of points is an absolutely \n"
          + "devastating tactic in 2 player games and very useful \n"
          + "with more players too. As with a lot of board games, \n"
          + "focusing on the end conditions is important. In this case, \n"
          + "filling up a row with tiles stops the game and determines the winner.";
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
