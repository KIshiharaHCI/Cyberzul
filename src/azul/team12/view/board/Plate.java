package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Plate extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int position;

  private int centerX;
  private int centerY;
  private int radius;

  private List<Tile> tileList;

  public Plate(int centerX, int centerY, int radius, int position,
      TileClickListener tileClickListener) {
    setLayout(new GridLayout(2, 2));
    setBorder(new LineBorder(Color.black));
    // setPreferredSize(new Dimension(radius * 2 + 10, radius * 2 + 10));

    this.centerX = centerX;
    this.centerY = centerY;
    this.radius = radius;
    this.position = position;
    this.tileList = new ArrayList<>();
    final GridLayout tilesLayout = new GridLayout(2, 2);
    setLayout(tilesLayout);
    if (this.position == 1) {
      URL imgURL1 = getClass().getClassLoader().getResource("img/black-tile.png");
      ImageIcon icon1 = new ImageIcon(imgURL1);
      BufferedImage resizedimage = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = resizedimage.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(icon1.getImage(), 0, 0, 60, 60, null);

      ImageIcon resizedIcon = new ImageIcon(resizedimage);
      final Tile tile1 = new Tile(1, 40, this.position, resizedIcon, tileClickListener);
      add(tile1);
      final Tile tile2 = new Tile(2, 40, this.position, resizedIcon, tileClickListener);
      add(tile2);
      final Tile tile3 = new Tile(3, 40, this.position, resizedIcon, tileClickListener);
      add(tile3);
      final Tile tile4 = new Tile(4, 40, this.position, resizedIcon, tileClickListener);
      add(tile4);
      this.tileList.add(tile1);
    }

//      Tile tile2 = new Tile(1, 5 + 50, 5 + 10, 40, this.position, icon.getImage());
//      this.add(tile2);
//      this.tileList.add(tile2);

  }

//  protected void paintComponent(Graphics g) {
//    Graphics2D g2d = (Graphics2D) g;
//    URL imgURL = getClass().getClassLoader().getResource("img/manufacturing-plate.png");
//    ImageIcon icon = new ImageIcon(imgURL);
//    g2d.drawImage(icon.getImage(),
//        centerX - radius, centerY - radius,
//        radius * 2, radius * 2, null);
//
//  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(radius * 2 + 10, radius * 2 + 10);
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public List<Tile> getTileList() {
    return tileList;
  }

  public void setTileList(List<Tile> tileList) {
    this.tileList = tileList;
  }

  public int getCenterX() {
    return centerX;
  }

  public void setCenterX(int centerX) {
    this.centerX = centerX;
  }

  public int getCenterY() {
    return centerY;
  }

  public void setCenterY(int centerY) {
    this.centerY = centerY;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }
}
