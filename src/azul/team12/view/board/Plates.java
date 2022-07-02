package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Plates extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int PLATE_SIZE = 116;

  private int numberOfPlates = 0;

  public Plates(int numberOfPlates, TileClickListener tileClickListener) {
    this.numberOfPlates = numberOfPlates;
    JPanel content = new JPanel();
    if (Math.ceil(numberOfPlates / 5f) == 1) {
      content.setLayout(new GridLayout(1, numberOfPlates));
      content.setOpaque(false);
      content.setPreferredSize(new Dimension(PLATE_SIZE * numberOfPlates,
          PLATE_SIZE));

      for (int i = 1; i <= this.numberOfPlates; i++) {
        Plate plate = new Plate(i, tileClickListener);
        content.add(plate);
      }
    } else {
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      content.setAlignmentX(0.5f);
      content.setOpaque(false);
      JPanel contentNorth = new JPanel(new GridLayout(1, 5));
      contentNorth.setOpaque(false);
      contentNorth.setPreferredSize(new Dimension(PLATE_SIZE * 5, PLATE_SIZE));
      JPanel contentSouth = new JPanel(new GridLayout(1, numberOfPlates - 5));
      contentSouth.setOpaque(false);
      contentSouth.setPreferredSize(new Dimension(PLATE_SIZE * (numberOfPlates - 5), PLATE_SIZE));
      contentSouth.setMaximumSize(new Dimension(PLATE_SIZE * (numberOfPlates - 5), PLATE_SIZE));
      content.add(contentNorth);
      content.add(contentSouth);

      for (int i = 1; i <= 5; i++) {
        Plate plate = new Plate(i, tileClickListener);
        contentNorth.add(plate);
      }

      for (int i = 1; i <= numberOfPlates - 5; i++) {
        Plate plate = new Plate(i + 5, tileClickListener);
        contentSouth.add(plate);
      }
    }
    setBackground(Color.DARK_GRAY);
    content.setBackground(Color.DARK_GRAY);

    add(content);
  }
}
