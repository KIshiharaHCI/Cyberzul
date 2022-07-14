package azul.team12.view;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.apache.logging.log4j.core.config.builder.api.Component;


public class IconButton extends JButton{

  private String iconPath;
  private int xPosition;
  private int yPosition;
  private int buttonWidth;
  private int buttonHeight;


  public IconButton(String path, int xPosition, int yPosition, int buttonWidth, int buttonHeight){
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
      Image icon = ImageIO.read(iconURL).getScaledInstance(buttonWidth, buttonHeight ,
          Image.SCALE_DEFAULT);
      this.setIcon(new ImageIcon(icon));
    }
    catch (Exception e) {
      e.printStackTrace();
      iconPath = null;
    }
    this.setBounds(xPosition, yPosition, buttonWidth, buttonHeight);
    this.setOpaque(false);
    this.setFocusPainted(false);
    this.setBorderPainted(false);
    this.setContentAreaFilled(false);
    this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0)); // Especially important
  }


}
