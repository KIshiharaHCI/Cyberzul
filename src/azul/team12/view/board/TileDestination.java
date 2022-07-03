package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.*;

/**
 * Superclass of TileDestinationPatternLines and TileDestinationWall. Saves the information about each
 * Tile such as the TileclickListener, ImageIcons of Tiles, column and row.
 */
public class TileDestination extends JPanel {

  private final String BLACK_TILE_PATH = "img/black-tile.png";
  private final String BLUE_TILE_PATH = "img/blue-tile.png";
  private final String RED_TILE_PATH = "img/red-tile.png";
  private final String WHITE_TILE_PATH = "img/white-tile.png";
  //TODO: make yellow, and in model yellow too
  private final String ORANGE_TILE_PATH = "img/yellow-tile.png";
  private final String STARTING_PLAYER_MARKER_PATH = "img/start-player-button.png";
  private final int cell;
  private final int row;
  private final int cellSize;

  private ImageIcon icon;

  private JLabel label;

  private ModelTile modelTile;

  public TileDestination(int cell, int row, int cellSize, TileClickListener tileClickListener,
      ModelTile modelTile) {
    setLayout(new GridLayout(1, 1));
    this.cell = cell;
    this.row = row;
    this.cellSize = cellSize;
    this.modelTile = modelTile;
    this.icon = modelTile.equals(ModelTile.EMPTY_TILE) ? null : setIcon(modelTile);
    this.setBackground(Color.WHITE);
    if (icon != null) {
      label = new JLabel(icon);
    } else {
      label = new JLabel("");
    }
    add(label);

    setPreferredSize(new Dimension(cellSize, cellSize));
    setMaximumSize(new Dimension(cellSize, cellSize));
    setMinimumSize(new Dimension(cellSize, cellSize));

    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    addMouseListener(tileClickListener);
  }
  public TileDestination(int cell, int row, int cellSize,ModelTile modelTile) {
    setLayout(new GridLayout(1, 1));
    this.cell = cell;
    this.row = row;
    this.cellSize = cellSize;
    this.modelTile = modelTile;
    this.icon = modelTile.equals(ModelTile.EMPTY_TILE) ? null : setTransparentIcon(modelTile);
    this.setBackground(Color.WHITE);
    if (icon != null) {
      label = new JLabel(icon);
    } else {
      label = new JLabel("");
    }
    add(label);

    setPreferredSize(new Dimension(cellSize, cellSize));
    setMaximumSize(new Dimension(cellSize, cellSize));
    setMinimumSize(new Dimension(cellSize, cellSize));

    setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }

  private ImageIcon setTransparentIcon(ModelTile modelTile) {
    switch (modelTile) {
      case BLACK_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(BLACK_TILE_PATH),0.5F);

      }
      case RED_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(RED_TILE_PATH),0.5F);
      }
      case BLUE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(BLUE_TILE_PATH),0.5F);
      }
      case WHITE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(WHITE_TILE_PATH),0.5F);
      }
      case ORANGE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(ORANGE_TILE_PATH),0.5F);
      }
      default -> throw new AssertionError("Unknown Tile!");

    }
  }

  public ImageIcon setIcon(ModelTile modelTile) {
    switch (modelTile) {
      case BLACK_TILE -> {
        return getResizedImageIcon(BLACK_TILE_PATH);
      }
      case RED_TILE -> {
        return getResizedImageIcon(RED_TILE_PATH);
      }
      case BLUE_TILE -> {
        return getResizedImageIcon(BLUE_TILE_PATH);
      }
      case WHITE_TILE -> {
        return getResizedImageIcon(WHITE_TILE_PATH);
      }
      case ORANGE_TILE -> {
        return getResizedImageIcon(ORANGE_TILE_PATH);
      }
      case STARTING_PLAYER_MARKER -> {
        return getResizedImageIcon(STARTING_PLAYER_MARKER_PATH);
      }
      default -> throw new AssertionError("Unknown Tile!");

    }
  }
  private ImageIcon getResizedImageIcon(String path) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(imgURL1).getImage()
            .getScaledInstance(this.cellSize, this.cellSize, Image.SCALE_DEFAULT));
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

  public ModelTile getModelTile() {
    return modelTile;
  }

  public void setModelTile(ModelTile modelTile) {
    this.modelTile = modelTile;
  }
}
