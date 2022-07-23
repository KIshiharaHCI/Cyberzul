package cyberzul.view.panels;

import static cyberzul.view.CyberzulView.getCustomFont;

import cyberzul.controller.Controller;
import cyberzul.model.CommonModel;
import cyberzul.model.Model;
import cyberzul.network.server.Server;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;




/** Lobby Screen that functions as the Lobby when a player wants to play via local network. */
public class NetworkLobbyScreen extends JLayeredPane {
  @Serial private static final long serialVersionUID = 17L;
  private static final int MIN_REQUIRED_PLAYERS = 3;
  private final Font customFont = getCustomFont();
  private final LinkedList<Players> disabledPlayers =
      new LinkedList<>(
          Arrays.asList(Players.PLAYER1, Players.PLAYER2, Players.PLAYER3, Players.PLAYER4));
  private final transient Controller controller;
  private final transient Model model;
  private final transient List<JLabel> labels = new ArrayList<>();
  private final Players lastLoggedInPlayer = Players.PLAYER1;
  transient List<JButton> nameInputButtons = new ArrayList<>(4);
  ImageIcon checkUnselected = imageLoader("img/check-unselected.png", 46, 40);
  ImageIcon checkSelected = imageLoader("img/check-selected.png", 46, 40);
  ImageIcon nickBannerUnselected = imageLoader("img/playerbanner-unselected.png", 300, 56);
  ImageIcon nickBannerSelected = imageLoader("img/playerbanner-selected.png", 300, 56);
  ImageIcon blankButtonUnselected = imageLoader("img/blank-button-unselected.png", 220, 55);
  ImageIcon blankButtonSelected = imageLoader("img/blank-button-selected.png", 220, 55);
  private JLabel banner;
  private JLabel setAddress;
  private JLabel ipAddressOnContainer;
  private JLabel enterServerIp;
  private JTextField ipInputField;
  private Dimension containerDimension;
  private Dimension popUpDimension;
  private transient BufferedImage image;
  private transient BufferedImage popUpImage;
  private JPanel container;
  private JPanel inputNickPopUp;
  private JPanel selectModePopUp;
  private JButton playGameButton;
  private JButton bulletButton;
  private String ipAddress;

  /**
   * Initializes all components for the Network Lobby.
   *
   * @param controller controller for the application
   * @param frameDimension determined by Cyberzulview.
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public NetworkLobbyScreen(Controller controller, Model model, Dimension frameDimension) {
    this.controller = controller;
    this.model = model;

    initializeProperties(frameDimension);
    initializeComponents();
    toggleAllChildPanelsVisible(this, false);
    selectModePopup();
  }

  private void selectModePopup() {
    selectModePopUp =
        new JPanel(null) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(popUpImage, 0, 0, null);
          }
        };
    selectModePopUp.setBounds(420, 200, popUpDimension.width, popUpDimension.height);
    add(selectModePopUp, Integer.valueOf(1));

    JLabel selectOption = new JLabel("Select option");

    selectOption.setFont(customFont.deriveFont(18f));
    selectOption.setForeground(Color.white);
    selectOption.setBounds(180, 100, 400, 30);
    selectModePopUp.add(selectOption);

    JButton createServerButton = new JButton(blankButtonUnselected);

    createServerButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            ipAddress = Server.start();
            model.setClientModelStrategy(ipAddress);
            controller.setMode(CommonModel.NETWORK_MODE);
            // TODO: setVisible other components
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            createServerButton.setIcon(blankButtonSelected);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            createServerButton.setIcon(blankButtonUnselected);
          }
        });
    createServerButton.setBorderPainted(false);
    createServerButton.setContentAreaFilled(false);
    createServerButton.setBounds(40, 170, 220, 55);

    createServerButton.setText("Create Server");
    createServerButton.setFont(customFont.deriveFont(15f));
    createServerButton.setForeground(Color.white);
    createServerButton.setHorizontalTextPosition(SwingConstants.CENTER);
    createServerButton.setVerticalTextPosition(SwingConstants.CENTER);

    selectModePopUp.add(createServerButton);

    JButton joinServerButton = new JButton(blankButtonUnselected);
    joinServerButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            joinServerButton.setVisible(false);
            createServerButton.setVisible(false);
            selectOption.setVisible(false);
            ipInputField.setVisible(true);
            enterServerIp.setVisible(true);
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            joinServerButton.setIcon(blankButtonSelected);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            joinServerButton.setIcon(blankButtonUnselected);
          }
        });
    joinServerButton.setBorderPainted(false);
    joinServerButton.setContentAreaFilled(false);
    joinServerButton.setBounds(280, 170, 220, 55);

    joinServerButton.setText("Join Server");
    joinServerButton.setFont(customFont.deriveFont(15f));
    joinServerButton.setForeground(Color.white);
    joinServerButton.setHorizontalTextPosition(SwingConstants.CENTER);
    joinServerButton.setVerticalTextPosition(SwingConstants.CENTER);
    selectModePopUp.add(joinServerButton);

    enterServerIp = new JLabel("Please enter the Server IP");
    enterServerIp.setFont(customFont);
    enterServerIp.setForeground(Color.white);
    enterServerIp.setBounds(160, 120, 400, 30);
    enterServerIp.setVisible(false);
    selectModePopUp.add(enterServerIp);

    JLabel maxChar = new JLabel("max 15 characters");
    maxChar.setFont(customFont.deriveFont(10f));
    maxChar.setForeground(Color.white);
    maxChar.setBounds(300, 180, 200, 20);
    maxChar.setVisible(false);
    selectModePopUp.add(maxChar);

    ipInputField = new JTextField(15);
    ipInputField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() != KeyEvent.VK_ENTER) {
              return;
            }
            event.consume();

            String ipAddress = ipInputField.getText();
            model.setClientModelStrategy(ipAddress);
            controller.setMode(CommonModel.NETWORK_MODE);

            ipInputField.setText(null);
          }
        });
    ipInputField.setBounds(140, 150, 300, 30);
    ipInputField.setFont(customFont);
    ipInputField.setVisible(false);
    selectModePopUp.add(ipInputField);

    selectModePopUp.setOpaque(false);
    showSelectModePrompt(true);
  }

  /**
   * Sets default properties for the JLayeredPane and initializes image assets used for painting
   * container class components.
   *
   * @param frameDimension given by Cyberzulview for screen sizing.
   */
  private void initializeProperties(Dimension frameDimension) {
    setLayout(null);
    setOpaque(false);

    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

    containerDimension =
        new Dimension((int) (frameDimension.width * 0.7), (int) (frameDimension.height * 0.7));
    popUpDimension = new Dimension(600, 374);

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/network-lobby.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
      image.getScaledInstance(
          containerDimension.width, containerDimension.height, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/hud.png");
      popUpImage = ImageIO.read(Objects.requireNonNull(imgUrl));
      popUpImage.getScaledInstance(popUpDimension.width, popUpDimension.height, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Initializes all Components added to this screen. */
  private void initializeComponents() {
    container =
        new JPanel(null) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
          }
        };
    container.setOpaque(false);
    container.setBounds(200, 80, containerDimension.width, containerDimension.height);
    add(container, Integer.valueOf(0));

    setInputNickPrompt();

    banner = new JLabel("Waiting for other players to connect ...");
    banner.setFont(customFont);
    banner.setBounds(180, 85, 400, 30);
    labels.add(banner);

    ipAddressOnContainer = new JLabel();
    ipAddressOnContainer.setForeground(Color.white);
    ipAddressOnContainer.setFont(customFont);
    ipAddressOnContainer.setBounds(250, 580, 200, 30);
    ipAddressOnContainer.setVisible(false);
    add(ipAddressOnContainer, Integer.valueOf(1));

    JLabel bulletMode = new JLabel("Bulletmode");
    bulletMode.setFont(customFont.deriveFont(15f));
    bulletMode.setForeground(Color.white);
    bulletMode.setBounds(250, 540, 400, 30);
    add(bulletMode, Integer.valueOf(1));

    bulletButton = new JButton(checkUnselected);
    bulletButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (checkUnselected.equals(bulletButton.getIcon())) {
              bulletButton.setIcon(checkSelected);
              controller.setBulletMode(true);
            } else {
              bulletButton.setIcon(checkUnselected);
              controller.setBulletMode(false);
            }
          }
        });
    bulletButton.setContentAreaFilled(false);
    bulletButton.setBorderPainted(false);
    bulletButton.setBounds(400, 530, 46, 40);
    add(bulletButton, Integer.valueOf(1));

    playGameButton = new JButton(imageLoader("img/start-game-button.png", 152, 50));
    playGameButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            controller.startGame();
          }
        });
    playGameButton.setBounds(550, 530, 152, 50);
    playGameButton.setContentAreaFilled(false);
    playGameButton.setBorderPainted(false);
    playGameButton.setEnabled(false);
    add(playGameButton, Integer.valueOf(1));

    initializeCheckBoxComponents();
    initializeInputNameBoxes();

    for (JButton component : nameInputButtons) {
      component.setContentAreaFilled(false);
      component.setBorderPainted(false);

      component.setHorizontalTextPosition(JButton.CENTER);
      component.setVerticalTextPosition(JButton.CENTER);

      customFont.deriveFont(30f);
      component.setFont(customFont);
      component.setForeground(Color.white);

      container.add(component);
    }
    for (JLabel label : labels) {
      container.add(label);
    }
  }

  /**
   * Initializes the name input buttons used for adding players to the game and adds them to a list
   * of components called in the parent method to add to the Panel.
   */
  private void initializeInputNameBoxes() {
    JButton nickInput1 = new JButton(nickBannerUnselected);
    nickInput1.setText("Player 1");
    nickInput1.setBounds(150, 130, 300, 56);
    nameInputButtons.add(nickInput1);

    JButton nickInput2 = new JButton(nickBannerUnselected);
    nickInput2.setText("Player 2");
    nickInput2.setBounds(150, 210, 300, 56);
    nameInputButtons.add(nickInput2);

    JButton nickInput3 = new JButton(nickBannerUnselected);
    nickInput3.setText("Player 3");
    nickInput3.setBounds(150, 290, 300, 56);
    nameInputButtons.add(nickInput3);

    JButton nickInput4 = new JButton(nickBannerUnselected);
    nickInput4.setText("Player 4");
    nickInput4.setBounds(150, 370, 300, 56);
    nameInputButtons.add(nickInput4);
  }

  /**
   * Creates Checkboxes that are placed next to the name input buttons and adds them to a list of
   * components later called to add to the Panel.
   */
  private void initializeCheckBoxComponents() {
    JLabel checkIcon1 = new JLabel(checkUnselected);
    checkIcon1.setBounds(475, 135, 46, 40);
    labels.add(checkIcon1);

    JLabel checkIcon2 = new JLabel(checkUnselected);
    checkIcon2.setBounds(475, 215, 46, 40);
    labels.add(checkIcon2);

    JLabel checkIcon3 = new JLabel(checkUnselected);
    checkIcon3.setBounds(475, 295, 46, 40);
    labels.add(checkIcon3);

    JLabel checkIcon4 = new JLabel(checkUnselected);
    checkIcon4.setBounds(475, 375, 46, 40);
    labels.add(checkIcon4);
  }

  /**
   * Called when a valid nickname was entered by the user and the button and checkbox need to be
   * updated.
   */
  public void updateinputField() {
    List<String> playerNamesList = controller.getPlayerNamesList();
    for (int i = 0; i < playerNamesList.size(); i++) {
      String player = playerNamesList.get(i);
      JButton button = (JButton) container.getComponent(i);
      button.setText(player);
      container.remove(i);
      container.add(button, i);

      disabledPlayers.remove(i);

      JLabel label = labels.get(i + 1);
      label.setIcon(checkSelected);
      labels.remove(i + 1);
      labels.add(i + 1, label);
    }

    updatePlayButton();
    showNickPrompt(false);
    validate();
  }

  /** Enables the PlayButton if at least two players have been connected. */
  private void updatePlayButton() {
    if (disabledPlayers.size() < MIN_REQUIRED_PLAYERS) {
      playGameButton.setVisible(true);
      playGameButton.setEnabled(true);
      banner.setText("Ready to start the game now!");
    } else {
      playGameButton.setEnabled(false);
    }
  }

  /** Creates the input nickname prompt. */
  private void setInputNickPrompt() {
    inputNickPopUp =
        new JPanel(null) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(popUpImage, 0, 0, null);
          }
        };
    inputNickPopUp.setBounds(420, 200, popUpDimension.width, popUpDimension.height);
    add(inputNickPopUp, Integer.valueOf(1));

    JLabel pleaseEnter = new JLabel("Please enter your nickname");
    pleaseEnter.setFont(customFont);
    pleaseEnter.setForeground(Color.white);
    pleaseEnter.setBounds(160, 120, 400, 30);
    inputNickPopUp.add(pleaseEnter);

    JLabel maxChar = new JLabel("max 15 characters");
    maxChar.setFont(customFont.deriveFont(10f));
    maxChar.setForeground(Color.white);
    maxChar.setBounds(300, 180, 200, 20);
    inputNickPopUp.add(maxChar);

    JLabel yourAddress = new JLabel("Your Server IP Address:");
    yourAddress.setFont(customFont);
    yourAddress.setForeground(Color.white);
    yourAddress.setBounds(140, 220, 400, 30);
    inputNickPopUp.add(yourAddress);

    setAddress = new JLabel();
    setAddress.setFont(customFont);
    setAddress.setForeground(Color.white);
    setAddress.setBounds(370, 220, 200, 30);
    inputNickPopUp.add(setAddress);

    JTextField inputField = new JTextField(15);
    inputField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() != KeyEvent.VK_ENTER) {
              return;
            }
            event.consume();

            String nickname = inputField.getText();
            controller.addPlayer(nickname);
            inputField.setText(null);
          }
        });
    inputField.setBounds(140, 150, 300, 30);
    inputField.setFont(customFont);

    inputNickPopUp.add(inputField);

    inputNickPopUp.setOpaque(false);
    showNickPrompt(false);
  }

  /**
   * Used to toggle the visibility of the input nickname prompt.
   *
   * @param toggle set visible or set invisible
   */
  private void showNickPrompt(boolean toggle) {
    inputNickPopUp.setVisible(toggle);
  }

  private void showSelectModePrompt(boolean toggle) {
    selectModePopUp.setVisible(toggle);
  }

  private void toggleAllChildPanelsVisible(Container parent, boolean toggle) {
    Component[] components = parent.getComponents();

    if (components.length > 0) {
      for (Component component : components) {
        if (component instanceof JPanel) {
          component.setVisible(toggle);
        } else if (component instanceof JButton) {
          component.setVisible(toggle);
        } else if (component instanceof JLabel) {
          component.setVisible(toggle);
        }
        if (component instanceof Container) {
          toggleAllChildPanelsVisible((Container) component, toggle);
        }
      }
    }
  }

  /** Removes the Input Area and shows the connected players. */
  public void updateUiAfterConnect() {
    toggleAllChildPanelsVisible(this, true);
    showNickPrompt(true);
    showSelectModePrompt(false);
    setAddress.setText(ipAddress);
    if (ipAddress != null) {
      ipAddressOnContainer.setText("Server IP " + ipAddress);
      ipAddressOnContainer.setVisible(true);
    }
  }

  /**
   * Used for the Bullet Check Box when.
   *
   * @param toggle is bulletMode checkbox toggled on or off.
   */
  public void updateBulletCheckBox(boolean toggle) {
    bulletButton.setIcon(toggle ? checkSelected : checkUnselected);
  }

  /**
   * Loads all image assets used by this class.
   *
   * @param path directory path in resource folder
   * @param width of image
   * @param height of image
   * @return image as ImageIcon
   */
  private ImageIcon imageLoader(String path, int width, int height) {
    URL resource = getClass().getClassLoader().getResource(path);
    ImageIcon icon = new ImageIcon(Objects.requireNonNull(resource));
    return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
  }

  private enum Players {
    PLAYER1,
    PLAYER2,
    PLAYER3,
    PLAYER4
  }
}
