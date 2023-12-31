package cyberzul.model.events;

/**
 * This event gets fired from the model to the listeners when someone clicks bullet mode button.
 * Informs listener.
 */
public class BulletModeChangedEvent extends GameEvent {

  public static final String EVENT_NAME = "BulletModeChangedEvent";

  private boolean isBulletModeActivated;

  public BulletModeChangedEvent(boolean isBulletModeActivated) {
    this.isBulletModeActivated = isBulletModeActivated;
  }

  @Override
  public String getName() {
    return EVENT_NAME;
  }

  /**
   * Tells the listeners if the bullet mode is activated.
   *
   * @return <code>true</code> if the bullet mode is activated. <code>false</code> else.
   */
  public boolean isBulletModeActivated() {
    return isBulletModeActivated;
  }
}
