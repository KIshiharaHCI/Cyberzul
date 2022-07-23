package cyberzul.network.client.messages;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A chat message sent by a user at a specific point in time.
 */
public class PlayerTextMessage implements Message {

  private final String nameOfSender;

  private final Date time;

  private final String content;

  /**
   * Constructor that includes the name of the sender and the text
   * which sent at a specific point in time.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  //time is indeed a reference to a mutable object, but it is still legal to just save the reference
  //here, because the class that calls this constructor doesn't store the reference itself.
  public PlayerTextMessage(String nameOfSender, Date time, String content) {
    this.nameOfSender = nameOfSender;
    this.time = time;
    this.content = content;
  }

  public String getNameOfSender() {
    return nameOfSender;
  }

  /**
   * The Time when the player sends the message.
   *
   * @return The Time when the message sent by the player.
   */
  public Date getTime() {
    Date copy = new Date();
    copy.setTime(time.getTime());
    return copy;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    String dateString =
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY)
            .format(time);
    return String.format("%s (%s): %s", nameOfSender, dateString, content);
  }
}
