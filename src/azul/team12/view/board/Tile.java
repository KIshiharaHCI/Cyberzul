package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tile extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int id;

  private int plateNr;
  private int position;

  private boolean selected = false;

  private int cellSize;

  private ImageIcon icon;
  private Image image;

  public Tile(int id, int cellSize, int plateNr,
      ImageIcon icon, TileClickListener tileClickListener) {
    this.id = id;
    this.cellSize = cellSize;
    this.plateNr = plateNr;
    this.icon = icon;
    this.image = icon.getImage();
    setBorder(BorderFactory.createLineBorder(Color.black));
    setToolTipText(id + "");
    add(new JLabel(icon));
    repaint();

    this.addMouseListener(tileClickListener);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(this.cellSize, this.cellSize);
  }
}
