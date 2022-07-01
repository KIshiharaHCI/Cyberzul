package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileDestination extends JPanel {

  private final int cell;
  private final int row;
  private final int cellSize;

  private ImageIcon icon;

  private JLabel label;

  public TileDestination(int cell, int row, int cellSize, TileClickListener tileClickListener,
      ImageIcon icon) {
    setLayout(new GridLayout(1, 1));
    this.cell = cell;
    this.row = row;
    this.cellSize = cellSize;
    this.icon = icon;
    this.setBackground(Color.WHITE);
    if (icon != null) {
      label = new JLabel(icon);
    } else {
      label = new JLabel("icon");
    }
    add(label);

    setPreferredSize(new Dimension(cellSize, cellSize));
    setMaximumSize(new Dimension(cellSize, cellSize));
    setMinimumSize(new Dimension(cellSize, cellSize));

    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    addMouseListener(tileClickListener);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(cellSize + 2, cellSize + 2);
  }

  public int getCell() {
    return cell;
  }

  public int getRow() {
    return row;
  }

  public int getCellSize() {
    return cellSize;
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

  public void setLabel(JLabel label) {
    this.label = label;
  }
}
