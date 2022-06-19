package view.board.playerBoard;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class PatternLines extends JPanel {
  List<PatternLinesStone> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  public PatternLines() {
    setLayout(new GridLayout(ROWS, COLS));
    initializeStones();

  }

  private void initializeStones() {
    this.stones = new ArrayList<>();
    for (int y = ROWS; y > 0; y--) {
      for (int x = COLS; x > 0; x--) {
        PatternLinesStone stone = new PatternLinesStone(x, y);
        setButtonBackground(stone);
        stones.add(stone);
        add(stone);
      }
    }


  }

  private void setButtonBackground(PatternLinesStone button) {
    button.setBackground(Color.GRAY);
  }
}
