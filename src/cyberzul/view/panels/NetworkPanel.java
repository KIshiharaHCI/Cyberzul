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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static cyberzul.view.CyberzulView.getCustomFont;

/**
 * Network Screen that functions as the Lobby when a player wants to play via Network.
 */
public class NetworkPanel extends JLayeredPane {
    @Serial
    private static final long serialVersionUID = 17L;
    private Controller controller;
    private Dimension containerDimension;
    private Dimension popUpDimension;
    private transient BufferedImage image;
    private transient BufferedImage popUpImage;
    private JPanel container;
    private JPanel inputNickPopUp;
    private JLabel banner;
    private final Font customFont = getCustomFont();
    private transient List<JLabel> labels = new ArrayList<>();
    transient List<JButton> nickInputButtons = new ArrayList<>(4);
    private enum nickInput {
            PLAYER1 ,
            PLAYER2,
            PLAYER3,
            PLAYER4
    }
    private nickInput lastEditPressed;
    ImageIcon checkUnselected = imageLoader("img/check-unselected.png", 46, 40);
    ImageIcon checkSelected = imageLoader("img/check-selected.png", 46, 40);
    ImageIcon nickBannerUnselected = imageLoader("img/playerbanner-unselected.png", 300, 56);
    ImageIcon nickBannerSelected = imageLoader("img/playerbanner-selected.png", 300, 56);

    /**
     * Initializes all components for the NetworkPanel.
     * @param controller
     * @param frameDimension
     */
    public NetworkPanel(Controller controller, Dimension frameDimension) {
        this.controller = controller;

        setProperties(frameDimension);
        initializeComponents();
        setBoundsForComponents();

        add(container, Integer.valueOf(0));
        add(inputNickPopUp, Integer.valueOf(1));
    }

    private void setProperties(Dimension frameDimension) {
        setLayout(null);
        setOpaque(false);

        setMinimumSize(frameDimension);
        setMaximumSize(frameDimension);

        containerDimension = new Dimension((int) (frameDimension.width * 0.7), (int)
                (frameDimension.height * 0.7));
        popUpDimension = new Dimension(600, 374);

        try {
            URL imgUrl = getClass().getClassLoader().getResource("img/network-lobby.png");
            image = ImageIO.read(Objects.requireNonNull(imgUrl));
            image.getScaledInstance(containerDimension.width, containerDimension.height,
                    Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            URL imgUrl = getClass().getClassLoader().getResource("img/hud.png");
            popUpImage = ImageIO.read(Objects.requireNonNull(imgUrl));
            popUpImage.getScaledInstance(popUpDimension.width, popUpDimension.height,
                    Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() {
        container = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        container.setOpaque(false);

        setInputNickPopUp();

        JLabel banner = new JLabel("Waiting for other players ... ");
        banner.setFont(customFont);
        banner.setBounds(180, 85, 400, 30);
        labels.add(banner);

        JLabel checkIcon1 = new JLabel(checkUnselected);
    checkIcon1.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              checkIcon1.setIcon(checkUnselected.equals(checkIcon1.getIcon()) ? checkSelected : checkUnselected);
          }
        });
        checkIcon1.setBounds(475 ,155, 46, 40);
        labels.add(checkIcon1);

        JLabel checkIcon2 = new JLabel(checkUnselected);
        checkIcon2.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkIcon2.setIcon(checkUnselected.equals(checkIcon2.getIcon()) ? checkSelected : checkUnselected);
            }
        });
        checkIcon2.setBounds(475 ,235, 46, 40);
        labels.add(checkIcon2);

        JLabel checkIcon3 = new JLabel(checkUnselected);
        checkIcon3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkIcon3.setIcon(checkUnselected.equals(checkIcon3.getIcon()) ? checkSelected : checkUnselected);
            }
        });
        checkIcon3.setBounds(475 ,315, 46, 40);
        labels.add(checkIcon3);

        JLabel checkIcon4 = new JLabel(checkUnselected);
        checkIcon4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkIcon4.setIcon(checkUnselected.equals(checkIcon4.getIcon()) ? checkSelected : checkUnselected);
            }
        });
        checkIcon4.setBounds(475 ,395, 46, 40);
        labels.add(checkIcon4);

        JButton nickInput1 = new JButton(nickBannerUnselected);
        nickInput1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInputAreaIfValidPress(nickInput.PLAYER1);
            }
        });
        nickInput1.setBounds(150,150, 300, 56);
        nickInputButtons.add(nickInput1);

        JButton nickInput2 = new JButton(nickBannerUnselected);
        nickInput2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInputAreaIfValidPress(nickInput.PLAYER2);
            }
        });
        nickInput2.setBounds(150,230, 300, 56);
        nickInputButtons.add(nickInput2);

        JButton nickInput3 = new JButton(nickBannerUnselected);
        nickInput3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInputAreaIfValidPress(nickInput.PLAYER3);
            }
        });
        nickInput3.setBounds(150,310, 300, 56);
        nickInputButtons.add(nickInput3);

        JButton nickInput4 = new JButton(nickBannerUnselected);
        nickInput4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInputAreaIfValidPress(nickInput.PLAYER4);
            }
        });
        nickInput4.setBounds(150,390, 300, 56);
        nickInputButtons.add(nickInput4);

        for (JButton component : nickInputButtons) {
            component.setContentAreaFilled(false);
            component.setBorderPainted(false);

            component.setHorizontalTextPosition(JButton.CENTER);
            component.setVerticalTextPosition(JButton.CENTER);

            customFont.deriveFont(30f);
            component.setFont(customFont);
            component.setForeground(Color.white);
            component.setText("Player");

            container.add(component);
        }
        for (JLabel label : labels) {
            container.add(label);
        }



    }

    private void showInputAreaIfValidPress(nickInput player) {
        if (lastEditPressed != null && lastEditPressed.equals(player.toString())) {
            return;
        }
        lastEditPressed = player;
        showPopUp(true);
    }

    private void setInputNickPopUp() {
        inputNickPopUp = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(popUpImage, 0, 0, null);
            }
        };

        JLabel pleaseEnter = new JLabel("Please enter your nickname");
        pleaseEnter.setFont(customFont);
        pleaseEnter.setForeground(Color.white);
        pleaseEnter.setBounds(160,100, 400, 30);
        inputNickPopUp.add(pleaseEnter);

        JTextField inputField = new JTextField(15);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                event.consume();
                updateinputField(inputField);
                //controller.postMessage(inputField.getText());
                inputField.setText(null);
            }
        });
        inputField.setBounds(140, 130, 300, 30);
        inputField.setFont(customFont);
        inputNickPopUp.add(inputField);


        inputNickPopUp.setOpaque(false);
        showPopUp(false);
    }

    private void updateinputField(JTextField inputField) {
        int index;
        if (lastEditPressed == null) {
            return;
        }
        JButton button = (JButton) container.getComponent(0);
        button.setText(inputField.getText());
        button.setEnabled(false);
        container.remove(0);
        container.add(button);
        showPopUp(false);
        validate();
    }

    private void setBoundsForComponents() {
        container.setBounds(200, 80, containerDimension.width, containerDimension.height);
        inputNickPopUp.setBounds(420, 200, popUpDimension.width, popUpDimension.height);
    }
    private void toggleBannerSelected(int i) {
        nickInputButtons.get(i).setIcon(nickBannerUnselected.equals(nickInputButtons.get(0)) ? nickBannerSelected : nickBannerUnselected);
    }
    private void updateComponent(int i, JComponent component) {
        removeAll();
        revalidate();
    }
    private void showPopUp(boolean toggle) {
        inputNickPopUp.setVisible(toggle);
    }

    private ImageIcon imageLoader(String path, int width, int height) {
        URL resource = getClass().getClassLoader().getResource(path);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(resource));
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
