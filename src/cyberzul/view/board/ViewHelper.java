package cyberzul.view.board;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This class should be used for example to avoid duplications in code.
 */
public class ViewHelper {

  public static final int PADDING = 2;

  /**
   * Sets properties to a line of {@link PatternLines} or {@link Wall}
   *
   * @param tileSize:   the size of a tile in the row.
   * @param cols:       number of columns in a row.
   * @param row:        number of the row.
   * @param currentRow: the current JPanel for the row.
   */
  static void setPropertiesOfCurrentRow(int tileSize, int cols, int row, JPanel currentRow) {
    final int hgapOrVgap = 2;
    final int halfOfHgapOrVgap = 1;
    currentRow.setLayout(new GridLayout(1, cols, hgapOrVgap, hgapOrVgap));
    currentRow.setAlignmentY(1.0f);
    currentRow.setMaximumSize(
        new Dimension(
            cols * (tileSize + hgapOrVgap) + hgapOrVgap,
            row * (tileSize + hgapOrVgap) + hgapOrVgap));
    currentRow.setBorder(
        BorderFactory.createEmptyBorder(
            halfOfHgapOrVgap, hgapOrVgap, halfOfHgapOrVgap, hgapOrVgap));
    currentRow.setOpaque(false);
  }

  /**
   * Sets properties of {@link PatternLines}.
   *
   * @param tileSize: the size of one tile that should be shown.
   */
  static void setProperties(int tileSize, int rows, int cols, JPanel panel) {
    // panel.setPreferredSize(new Dimension((tileSize + PADDING) * cols + PADDING,
    // (tileSize + PADDING) * rows + PADDING));
    panel.setOpaque(false);
    panel.setMaximumSize(
        new Dimension(
            (tileSize + PADDING) * cols + PADDING, (tileSize + PADDING) * rows + PADDING));
    panel.setMinimumSize(
        new Dimension(
            (tileSize + PADDING) * cols + PADDING, (tileSize + PADDING) * rows + PADDING));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(1.0f);
    panel.setAlignmentY(1.0f);
  }
}
