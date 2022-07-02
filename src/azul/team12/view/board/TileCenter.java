package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileCenter extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int cellSize;
  private int row;
  private int column;

  private ImageIcon icon;

  private JLabel label;

  public TileCenter(int row, int col, int cellSize,
      ImageIcon icon, TileClickListener tileClickListener) {
    setLayout(new GridLayout(1, 1));
    this.row = row;
    this.column = col;
    this.cellSize = cellSize;
    this.icon = icon;
    label = icon != null ? new JLabel(icon) : new JLabel("");
    add(label);
    label.setVisible(icon != null);

    this.addMouseListener(tileClickListener);
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

  public int getCellSize() {
    return cellSize;
  }
}

