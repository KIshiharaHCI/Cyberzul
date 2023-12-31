package cyberzul.shared;

import cyberzul.model.FactoryDisplay;
import cyberzul.model.ManipulableFactoryDisplay;
import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import cyberzul.network.shared.JsonMessage;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test the JsonMessage Methods.
 */
public class JsonMessageTest {

  /**
   * Tests if the createObjectContainingOfferings method puts the content of offerings in the right
   * order in the array.
   */
  @Test
  public void testCreateArrayContainingOfferings() {
    FactoryDisplay factoryDisplay = new ManipulableFactoryDisplay(ModelTile.RED_TILE);
    FactoryDisplay factoryDisplay1 = new ManipulableFactoryDisplay(ModelTile.BLUE_TILE);
    List<Offering> offeringList = new ArrayList<>();
    offeringList.add(factoryDisplay);
    offeringList.add(factoryDisplay1);

    JSONArray jsonArray = JsonMessage.parseOfferingsToJsonArray(offeringList);
    String testString =
        "[[\"red tile\",\"red tile\",\"red tile\",\"red tile\"],[\"blue tile\",\"blue tile\","
            + "\"blue tile\",\"blue tile\"]]";

    Assertions.assertTrue(jsonArray.toString().equals(testString));
  }
}
