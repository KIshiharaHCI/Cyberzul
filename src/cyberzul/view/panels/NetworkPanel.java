package cyberzul.view.panels;

import cyberzul.controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Network Screen that functions as the Lobby when a player wants to play via Network.
 */
public class NetworkPanel extends JLayeredPane {
    @Serial
    private static final long serialVersionUID = 17L;
    private Controller controller;
    private Dimension containerDimension;
    private transient BufferedImage image;
    private JPanel container;
    List<JComponent> nickInputFields = new ArrayList<>(4);

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
    }

    private void setProperties(Dimension frameDimension) {
        setLayout(null);
        setOpaque(false);

        setMinimumSize(frameDimension);
        setMaximumSize(frameDimension);

        containerDimension = new Dimension((int) (frameDimension.width * 0.7), (int)
                (frameDimension.height * 0.7));

        try {
            URL imgUrl = getClass().getClassLoader().getResource("img/network-lobby.png");
            image = ImageIO.read(Objects.requireNonNull(imgUrl));
            image.getScaledInstance(containerDimension.width, containerDimension.height,
                    Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() {
        container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        container.setOpaque(false);
        //TODO: add back hardcoded JButtons
        int verticalPos = 100;
        for (int i = 0; i < nickInputFields.size(); i++) {
            JButton nickInput = new JButton(imageLoader("img/playerbanner-unselected.png", 300, 56));
            nickInputFields.add(nickInput);
            container.add(nickInput);

            nickInput.setBounds(0, verticalPos, 300, 56);
            verticalPos += 300;
        }


    }

    private void setBoundsForComponents() {
        container.setBounds(200, 80, containerDimension.width, containerDimension.height);
    }

    private ImageIcon imageLoader(String path, int width, int height) {
        URL resource = getClass().getClassLoader().getResource(path);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(resource));
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
