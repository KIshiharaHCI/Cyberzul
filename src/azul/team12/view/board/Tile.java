package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tile extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int id;

  private int plateId;
  private int cellSize;

  private ImageIcon icon;

  private JLabel label;

  public Tile(int id, int cellSize, int plateId,
      ImageIcon icon, TileClickListener tileClickListener) {
    setLayout(new GridLayout(1, 1));
    this.id = id;
    this.cellSize = cellSize;
    this.plateId = plateId;
    this.icon = icon;
    setBorder(BorderFactory.createLineBorder(Color.black));
    setToolTipText(id + "");
    label = icon != null ? new JLabel(icon) : new JLabel("");
    add(label);

    this.addMouseListener(tileClickListener);
  }

  public int getId() {
    return id;
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public void setIcon(ImageIcon icon) {
    this.icon = icon;
  }

  public JLabel getLabel() {
    return label;
  }
}
