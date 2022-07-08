package azul.team12.view.board;

import azul.team12.model.ModelTile;
import javax.swing.ImageIcon;

public class TileDestinationWallTransparent extends TileDestination {

  public TileDestinationWallTransparent(int cell, int row,
      ModelTile modelTile) {
    super(cell, row, modelTile);
  }

  @Override
  public ImageIcon getLabelIcon(ModelTile modelTile) {
    switch (modelTile) {
      case BLACK_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(BLACK_TILE_PATH), 0.25F);

      }
      case RED_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(RED_TILE_PATH), 0.25F);
      }
      case BLUE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(BLUE_TILE_PATH), 0.25F);
      }
      case WHITE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(WHITE_TILE_PATH), 0.25F);
      }
      case ORANGE_TILE -> {
        return new TransparentImageIcon(getResizedImageIcon(ORANGE_TILE_PATH), 0.25F);
      }
      default -> throw new AssertionError("Unknown Tile!");

    }
  }


}
