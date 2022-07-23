package cyberzul.view.board;

import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.Timer;

/**
 * Timer for showing time for one turn in the view.
 */
public class TurnCountDownTimer extends Timer {
  @Serial
  private static final long serialVersionUID = 1L;
  private int timerValue;

  /**
   * Creates a {@code Timer} and initializes both the initial delay and between-event delay to
   * {@code delay} milliseconds. If {@code delay} is less than or equal to zero, the timer fires as
   * soon as it is started. If <code>listener</code> is not <code>null</code>, it's registered as an
   * action listener on the timer.
   *
   * @param delay    milliseconds for the initial and between-event delay
   * @param listener an initial listener; can be <code>null</code>
   * @see #addActionListener
   * @see #setInitialDelay
   * @see #setRepeats
   */
  public TurnCountDownTimer(int delay, ActionListener listener) {
    super(delay, listener);
    timerValue = 12;
  }

  @Override
  public void start() {
    super.start();
    this.timerValue = 12;
  }

  @Override
  public void stop() {
    super.stop();
    this.timerValue = 0;
  }

  public int getTimerValue() {
    return timerValue;
  }

  public void setTimerValue(int timerValue) {
    this.timerValue = timerValue;
  }
}
