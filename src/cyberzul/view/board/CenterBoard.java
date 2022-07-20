package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.Offering;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.Serial;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/** The center of the table ("Tischmitte"). */
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
  private JPanel activeUserButtonsPanel;
  private JButton forfeitButton;
  private JButton cancelButton;
  private JButton restartButton;
  private Dimension panelDimension;
  private Dimension topPanelDimension;
  private Dimension bottomPanelDimension;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(
      Controller controller, TileClickListener tileClickListener, Dimension panelDimension) {
    this.controller = controller;

    this.tileClickListener = tileClickListener;
    this.panelDimension = panelDimension;

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

  private void createActiveUserButtonsPanel() {
    activeUserButtonsPanel = new JPanel();
    activeUserButtonsPanel.setAlignmentY(Box.BOTTOM_ALIGNMENT);
    activeUserButtonsPanel.setLayout(new BoxLayout(activeUserButtonsPanel, BoxLayout.Y_AXIS));
    activeUserButtonsPanel.setOpaque(false);
    Double rightSideBarHeight = panelDimension.height * 0.3;
    int rightSideBarHeightInt = rightSideBarHeight.intValue();
    Dimension rightSideBarDimension =
        new Dimension(panelDimension.width, rightSideBarHeightInt);
    setMaximumSize(rightSideBarDimension);
    setMinimumSize(rightSideBarDimension);

    // TODO: replace with Align Bottom for the buttons if it works
    activeUserButtonsPanel.add(Box.createVerticalStrut(300));

    initializeButtons();
    add(activeUserButtonsPanel);
  }

  private void initializeButtons() {
    forfeitButton = new JButton();
    setButtonProperties(forfeitButton, "img/forfeit-button.png");

    forfeitButton.addActionListener(
        event -> controller.replacePlayerByAi(controller.getNickOfActivePlayer()));

    cancelButton = new JButton();
    setButtonProperties(cancelButton, "img/cancel-button.png");

    cancelButton.addActionListener(event -> controller.cancelGameForAllPlayers());

    restartButton = new JButton();
    setButtonProperties(restartButton, "img/restart-button.png");

    restartButton.addActionListener(event -> controller.restartGame());
  }

  /**
   * Loads the button image and sets it on the button and removes the background of the JButton.
   *
   * @param button the JButton to decorate
   * @param path file path for the custom button image
   */
  private void setButtonProperties(JButton button, String path) {
    URL imgUrl1 = getClass().getClassLoader().getResource(path);
    ImageIcon img =
        new ImageIcon(
            new ImageIcon(Objects.requireNonNull(imgUrl1))
                .getImage()
                .getScaledInstance(140, 52, Image.SCALE_DEFAULT));
    button.setIcon(img);
    button.setBorder(null);
    button.setContentAreaFilled(false);

    activeUserButtonsPanel.add(Box.createVerticalStrut(40));
    activeUserButtonsPanel.add(button);
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
    topPanelDimension =
        new Dimension(panelDimension.width, topPanelHeightInt);
    bottomPanelDimension =
        new Dimension(panelDimension.width, computePanelWidthInt);
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
            controller,
            tileClickListener,
            controller.getNickOfActivePlayer(),
            bottomPanelDimension);

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

  public TableCenterPanel getTableCenterPanel() {
    return tableCenterPanel;
  }
}
