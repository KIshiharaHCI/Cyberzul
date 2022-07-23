package cyberzul.model.events;

/**
 * This event gets fired to the listeners whenever an invalid IP address (or code that encodes
 * for an IP address) is used in an attempt to connect to a server.
 */
public class InvalidIpv4AddressEvent extends GameEvent {

  public static final String EVENT_NAME = "InvalidIPv4AddressEvent";

  @Override
  public String getName() {
    return EVENT_NAME;
  }
}
