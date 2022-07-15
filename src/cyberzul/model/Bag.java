package cyberzul.model;

import java.util.List;

/**
 * Super class for FactoryDisplays, Table Center, BagToDrawNewTiles, and BagToStoreUsedTiles.
 * Intentionally not an Interface but an abstract class, because that way, the abstract method can
 * be package friendly instead of public. Ensures encapsulation.
 */
public abstract class Bag {

    Bag() {
        initializeContent();
    }

    /**
     * Set up the initial content of this bag.
     */
    abstract void initializeContent();

    /**
     * Safely return the content of this bag, without enabling someone to tinker with its content.
     *
     * @return a copy of the content of this bag.
     */
    public abstract List<ModelTile> getContent();

}
