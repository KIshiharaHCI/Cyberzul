package cyberzul.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class IconButton extends JButton {

    private String iconPath;
    private final int xPosition;
    private final int yPosition;
    private final int buttonWidth;
    private final int buttonHeight;


    public IconButton(String path, int xPosition, int yPosition, int buttonWidth, int buttonHeight) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.iconPath = path;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        createIconButton();
    }

    private void createIconButton() {
        URL iconURL = getClass().getClassLoader().getResource(iconPath);
        try {
            Image icon = ImageIO.read(iconURL).getScaledInstance(buttonWidth, buttonHeight,
                    Image.SCALE_DEFAULT);
            this.setIcon(new ImageIcon(icon));
        } catch (Exception e) {
            e.printStackTrace();
            iconPath = null;
        }
        this.setBounds(xPosition, yPosition, buttonWidth, buttonHeight);
        this.setOpaque(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    }


}
