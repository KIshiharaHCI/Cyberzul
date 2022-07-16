package cyberzul.network.client.messages;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayerTextMessage extends Message {

  private final String nameOfSender;

  private final Date time;

  private final String content;

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
