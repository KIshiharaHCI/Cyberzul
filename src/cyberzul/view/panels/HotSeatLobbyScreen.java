package cyberzul.view.panels;

import cyberzul.controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.List;
import java.util.*;

import static cyberzul.view.CyberzulView.getCustomFont;

/**
 * Lobby Screen that functions as the Lobby when a player wants to play on Hot Seat Mode.
 */
public class HotSeatLobbyScreen extends JLayeredPane {
  @Serial
  private static final long serialVersionUID = 17L;
  private static final int MIN_REQUIRED_PLAYERS = 3;
  private final Font customFont = getCustomFont();
  private final HashSet<Players> enabledPlayers = new HashSet<>();
  private final HashSet<Players> disabledPlayers =
      new HashSet<>(
          Arrays.asList(Players.PLAYER1, Players.PLAYER2, Players.PLAYER3, Players.PLAYER4));
  private final transient Controller controller;
  private final transient List<JLabel> labels = new ArrayList<>();
  transient List<JButton> nameInputButtons = new ArrayList<>(4);
  ImageIcon checkUnselected = imageLoader("img/check-unselected.png", 46, 40);
  ImageIcon checkSelected = imageLoader("img/check-selected.png", 46, 40);
  ImageIcon nickBannerUnselected = imageLoader("img/playerbanner-unselected.png", 300, 56);
  ImageIcon nickBannerSelected = imageLoader("img/playerbanner-selected.png", 300, 56);
  private Dimension containerDimension;
  private Dimension popUpDimension;
  private transient BufferedImage image;
  private transient BufferedImage popUpImage;
  private JPanel container;
  private JPanel inputNickPopUp;
  private JButton playGameButton;
  private Players lastLoggedInPlayer;

  /**
   * Initializes all components for the HotSeatLobby.
   *
   * @param controller     controller for the application
   * @param frameDimension determined by Cyberzulview.
   */
  public HotSeatLobbyScreen(Controller controller, Dimension frameDimension) {
    this.controller = controller;

    initializeProperties(frameDimension);
    initializeComponents();
    setBoundsForComponents();
    add(container, Integer.valueOf(0));
    add(inputNickPopUp, Integer.valueOf(1));
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
      URL imgUrl = getClass().getClassLoader().getResource("img/hotseat-lobby.png");
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

  /**
   * Initializes all Components added to this screen.
   */
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

    setInputNickPrompt();

    JLabel banner = new JLabel("Please add players to start game");
    banner.setFont(customFont);
    banner.setBounds(180, 85, 400, 30);
    labels.add(banner);

      JLabel bulletMode = new JLabel("Bulletmode");
      bulletMode.setFont(customFont.deriveFont(15f));
      bulletMode.setForeground(Color.white);
      bulletMode.setBounds(250, 540, 400, 30);
      add(bulletMode);

      JButton bulletButton = new JButton(checkUnselected);
      bulletButton.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (checkUnselected.equals(bulletButton.getIcon())) {
                  bulletButton.setIcon(checkSelected);
                  controller.setBulletMode(true);
                  //TODO: setBullet(false) in Constructor if default (false) is not set in model
                  //TODO: controller.setBullet(true)
              } else {
                  bulletButton.setIcon(checkUnselected);
                  controller.setBulletMode(false);
                  //TODO: controller.setBullet(false)
              }
          }
      });
      bulletButton.setContentAreaFilled(false);
      bulletButton.setBorderPainted(false);
      bulletButton.setBounds(400, 530, 46, 40);
      add(bulletButton);

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
    add(playGameButton);

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
    nickInput1.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            showInputAreaIfValidPress(Players.PLAYER1);
          }
        });
    nickInput1.setBounds(150, 130, 300, 56);
    nameInputButtons.add(nickInput1);

    JButton nickInput2 = new JButton(nickBannerUnselected);
    nickInput2.setText("Player 2");
    nickInput2.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            showInputAreaIfValidPress(Players.PLAYER2);
          }
        });
    nickInput2.setBounds(150, 210, 300, 56);
    nameInputButtons.add(nickInput2);

    JButton nickInput3 = new JButton(nickBannerUnselected);
    nickInput3.setText("Player 3");
    nickInput3.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            showInputAreaIfValidPress(Players.PLAYER3);
          }
        });
    nickInput3.setBounds(150, 290, 300, 56);
    nameInputButtons.add(nickInput3);

    JButton nickInput4 = new JButton(nickBannerUnselected);
    nickInput4.setText("Player 4");
    nickInput4.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            showInputAreaIfValidPress(Players.PLAYER4);
          }
        });
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
   * Returns the input Button corresponding to the Player.
   *
   * @param player the edited player
   * @return button with the text set as the player name.
   */
  private JButton getPlayerInputButton(Players player) {
    int index = Integer.parseInt(player.toString().substring(6, 7)) - 1;
    JButton button = (JButton) container.getComponent(index);
    return button;
  }

  /**
   * shows the "Please enter name" prompt if the player has not been enabled yet.
   *
   * @param player selected to edit
   */
  private void showInputAreaIfValidPress(Players player) {
    if (enabledPlayers != null && enabledPlayers.contains(player)) {
      return;
    }
    enabledPlayers.add(player);
    lastLoggedInPlayer = player;
    showNickPrompt(true);
  }

  /**
   * Called when a valid nickname was entered by the user and the button and checkbox need to be
   * updated.
   *
   * @param nickname input entered by user
   */
  private void updateinputField(String nickname) {
    if (enabledPlayers == null) {
      return;
    }
    JButton button = getPlayerInputButton(lastLoggedInPlayer);

    controller.addPlayer(nickname);
    button.setText(nickname);
    button.setIcon(nickBannerSelected);
    int index = Integer.parseInt(lastLoggedInPlayer.toString().substring(6, 7)) - 1;
    container.remove(index);
    container.add(button, index);

    disabledPlayers.remove(lastLoggedInPlayer);
    JLabel label = labels.get(index + 1);
    label.setIcon(checkSelected);
    labels.remove(index + 1);
    labels.add(index + 1, label);

    updatePlayButton();
    showNickPrompt(false);
    validate();
  }

  /**
   * Enables the PlayButton if at least two players have been connected.
   */
  private void updatePlayButton() {
    playGameButton.setEnabled(disabledPlayers.size() < MIN_REQUIRED_PLAYERS);
  }

  /**
   * Used to set the bounds for container class components.
   */
  private void setBoundsForComponents() {
    container.setBounds(200, 80, containerDimension.width, containerDimension.height);
    inputNickPopUp.setBounds(420, 200, popUpDimension.width, popUpDimension.height);
  }

  /**
   * Creates the input nickname prompt.
   */
  private void setInputNickPrompt() {
    inputNickPopUp =
        new JPanel(null) {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(popUpImage, 0, 0, null);
          }
        };

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
            if (!controller.getPlayerNamesList().contains(nickname)) {
              updateinputField(nickname);
            } else {
              // TODO: Show popup
            }
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

  /**
   * Loads all image assets used by this class.
   *
   * @param path   directory path in resource folder
   * @param width  of image
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
