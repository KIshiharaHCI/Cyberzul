package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Plate extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final int TILE_SIZE = 40;

  private final int PLATE_SIZE = 116;

  private final int PADDING_OUTSIDE = 16;
  private final int PADDING_BETWEEN_TILES = 4;
  private int id;
  private List<Tile> tileList;

  public Plate(int id, TileClickListener tileClickListener) {
    setLayout(new GridLayout(1, 1, 40, 40));
    JLabel plateImageLabel = new JLabel(
        getResizedImageIcon("img/manufacturing-plate.png", PLATE_SIZE));
    setOpaque(false);
    // GridLayout plateLayout = new GridLayout(2, 2);
    plateImageLabel.setBounds(0, 0, PLATE_SIZE, PLATE_SIZE);
    // plateImageLabel.setLayout(plateLayout);
    this.id = id;
    this.tileList = new ArrayList<>();

    final Tile tile1 = new Tile(1, TILE_SIZE, this.id,
        getResizedImageIcon("img/black-tile.png", TILE_SIZE),
        tileClickListener);
    tile1.setBounds(PADDING_OUTSIDE, PADDING_OUTSIDE, TILE_SIZE, TILE_SIZE);
    plateImageLabel.add(tile1);
    final Tile tile2 = new Tile(2, TILE_SIZE, this.id,
        getResizedImageIcon("img/red-tile.png", TILE_SIZE),
        tileClickListener);
    tile2.setBounds(PADDING_OUTSIDE + TILE_SIZE + PADDING_BETWEEN_TILES, PADDING_OUTSIDE, TILE_SIZE,
        TILE_SIZE);
    plateImageLabel.add(tile2);
    final Tile tile3 = new Tile(3, TILE_SIZE, this.id,
        getResizedImageIcon("img/blue-tile.png", TILE_SIZE),
        tileClickListener);
    tile3.setBounds(PADDING_OUTSIDE, PADDING_OUTSIDE + TILE_SIZE + PADDING_BETWEEN_TILES, TILE_SIZE,
        TILE_SIZE);
    plateImageLabel.add(tile3);
    final Tile tile4 = new Tile(4, TILE_SIZE, this.id,
        getResizedImageIcon("img/yellow-tile.png", TILE_SIZE),
        tileClickListener);
    tile4.setBounds(PADDING_OUTSIDE + TILE_SIZE + PADDING_BETWEEN_TILES,
        PADDING_OUTSIDE + TILE_SIZE + PADDING_BETWEEN_TILES, TILE_SIZE, TILE_SIZE);
    plateImageLabel.add(tile4);
    this.tileList.add(tile1);
    add(plateImageLabel);

  }

  private ImageIcon getResizedImageIcon(String path, int size) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(imgURL1).getImage()
            .getScaledInstance(size, size, Image.SCALE_DEFAULT));
  }

//  private ImageIcon getResizedImageIcon(String path, int size) {
//    URL imgURL1 = getClass().getClassLoader().getResource(path);
//    ImageIcon icon1 = new ImageIcon(imgURL1);
//    BufferedImage resizedimage = new BufferedImage(size, size,
//        BufferedImage.TYPE_INT_RGB);
//    Graphics2D g2 = resizedimage.createGraphics();
//    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//    g2.drawImage(icon1.getImage(), 0, 0, size, size, null);
//
//    return new ImageIcon(resizedimage);
//  }

}
