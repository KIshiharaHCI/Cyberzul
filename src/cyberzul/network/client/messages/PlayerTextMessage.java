package cyberzul.network.client.messages;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * //TODO: Xue.
 */
public class PlayerTextMessage extends Message {

  private final String nameOfSender;

  private final Date time;

  private final String content;

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
