package cyberzul.model;

/**
 * The main interface of the Azul model for the graphical user-interface. It provides all necessary
 * methods for accessing and manipulating the data such that a game can be played successfully.
 */
public interface Model extends ModelStrategy {

    int GAME_MODEL = 0;
    int CLIENT_MODEL = 1;

    /**
     * Tell the ModelStrategyChooser what kind of strategy he should use.
     *
     * @param strategy
     */
    void setStrategy(int strategy);

}
