package cyberzul.model;

/**
 * The main interface of the Azul model for the graphical user-interface. It provides all necessary
 * methods for accessing and manipulating the data such that a game can be played successfully.
 */
public interface Model extends ModelStrategy {

  /**
   * Tell the ModelStrategyChooser that it should behave like a ClientModel.
   *
   * @param ipAddress the IPv4 Address that the ClientModel needs to connect to the Server.
   */
  void setClientModelStrategy(String ipAddress);

  /**
   * Tell the ModelStrategyChooser that it should behave like a GameModel.
   */
  void setGameModelStrategy();

  /**
   * Informs if the ModelStrategyChooser already chose a strategy.
   *
   * @return <code>true</code> if the ModelStrategyChooser chose a Strategy already.
   *         <code>false</code> else.
   */
  boolean isStrategyChosen();
}
