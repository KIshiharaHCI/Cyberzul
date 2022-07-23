package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.Offering;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Serial;
import java.util.List;
import javax.swing.JPanel;

/**
 * Contains active player board, the factory displays and the table center (in the center of the
 * {@link GameBoard}).
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification =
        "We are aware that data "
            + "encapsulation is violated here and that this is in principle bad. "
            + "However, as here just information of the view is possible to be changed from "
            + "an external source and the model is safe, we think it is ok to suppress "
            + "this warning.")
public class CenterBoard extends JPanel {
  @Serial private static final long serialVersionUID = 5L;
  private final transient Controller controller;
  private final JPanel boardAndPlatesAndTablePanel;
  private final transient TileClickListener tileClickListener;
  private PlatesPanel platesPanel;
  private TableCenterPanel tableCenterPanel;
  private PlayerBoard currentPlayerBoard;
  private JPanel platesAndTableCenterPanel;
  private Dimension panelDimension;
  private Dimension topPanelDimension;
  private Dimension bottomPanelDimension;

  private final String loggedInPlayer;
  private final boolean hotSeatMode;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(
      Controller controller,
      TileClickListener tileClickListener,
      String nickOfCenterBoardPlayer,
      boolean hotSeatMode,
      Dimension panelDimension) {
    this.controller = controller;

    this.tileClickListener = tileClickListener;
    this.panelDimension = panelDimension;
    this.loggedInPlayer = nickOfCenterBoardPlayer;
    this.hotSeatMode = hotSeatMode;

    computePanelSizes();
    setProperties();

    boardAndPlatesAndTablePanel = new JPanel(new BorderLayout());
    boardAndPlatesAndTablePanel.setMinimumSize(panelDimension);
    boardAndPlatesAndTablePanel.setMaximumSize(panelDimension);
    boardAndPlatesAndTablePanel.setOpaque(false);
    setPlatesAndTableCenterPanel();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

    add(boardAndPlatesAndTablePanel);
  }

  private String getCurrentPlayer() {
    String playerName = loggedInPlayer;
    if (hotSeatMode) {
      playerName = controller.getNickOfActivePlayer();
    }
    return playerName;
  }

  /**
   * Calculates the relative Size for "this", PlateAndTableCenterPanel and PlayerBoard based on the
   * Dimensions of the GameBoard.
   */
  private void computePanelSizes() {
    Double computePanelWidth = panelDimension.width * 0.45;
    int computePanelWidthInt = computePanelWidth.intValue();
    panelDimension = new Dimension(computePanelWidthInt, panelDimension.height);
    Double topPanelHeight = panelDimension.height * 0.38;
    int topPanelHeightInt = topPanelHeight.intValue();
    topPanelDimension = new Dimension(panelDimension.width, topPanelHeightInt);
    bottomPanelDimension = new Dimension(panelDimension.width, computePanelWidthInt);
  }

  private void setProperties() {
    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);
    setOpaque(false);
  }

  private void setPlatesAndTableCenterPanel() {
    platesAndTableCenterPanel = new JPanel(new BorderLayout());
    platesAndTableCenterPanel.setMinimumSize(topPanelDimension);
    platesAndTableCenterPanel.setMaximumSize(topPanelDimension);
    platesAndTableCenterPanel.setOpaque(false);
    boardAndPlatesAndTablePanel.add(platesAndTableCenterPanel, BorderLayout.NORTH);
  }

  /** Used by Constructor and CyberzulView to create and add a new PlayerBoard panel. */
  void createNewPlayerBoard() {
    currentPlayerBoard =
        new ActivePlayerBoard(
            controller, tileClickListener, getCurrentPlayer(), bottomPanelDimension);

    boardAndPlatesAndTablePanel.add(currentPlayerBoard, BorderLayout.CENTER);
  }

  /** Used by Constructor and CyberzulView to create and add a new Plates panel. */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays =
        controller.getOfferings().subList(1, controller.getOfferings().size());
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(
        new Dimension((int) (topPanelDimension.width * 0.6), topPanelDimension.height));
    platesAndTableCenterPanel.add(platesPanel, BorderLayout.CENTER);
  }

  /** Used by Constructor and CyberzulView to create and add a new TableCenter panel. */
  void createNewTableCenter() {
    tableCenterPanel = new TableCenterPanel(controller, tileClickListener, topPanelDimension);
    platesAndTableCenterPanel.add(tableCenterPanel, BorderLayout.EAST);
  }

  /** Removes all Panels of the last player who ended their turn. */
  public void removeAllPanels() {
    platesAndTableCenterPanel.remove(tableCenterPanel);
    platesAndTableCenterPanel.remove(platesPanel);
    boardAndPlatesAndTablePanel.remove(currentPlayerBoard);
  }

  /**
   * Updates the current state of the CenterBoard by clearing and adding updated instances of each
   * widget.
   */
  public void updateCenterBoard() {
    removeAllPanels();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();
  }

  /**
   * Used by TileClickListener to get the current instance of TableCenter.
   *
   * @return the table center panel
   */
  public TableCenterPanel getTableCenterPanel() {
    return tableCenterPanel;
  }
}
