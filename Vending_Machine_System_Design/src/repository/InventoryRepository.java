package repository;

import model.Inventory;
import model.ItemShelf;

public class InventoryRepository {
    private Inventory inventory;

    public InventoryRepository() {
        this.inventory = new Inventory();
    }

    public void save(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory get() {
        return inventory;
    }

    public ItemShelf getShelf(int code) {
        return inventory.getShelfByCode(code);
    }
}
