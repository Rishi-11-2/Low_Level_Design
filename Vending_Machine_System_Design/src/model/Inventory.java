package model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<ItemShelf> shelves;

    public Inventory() {
        this.shelves = new ArrayList<>();
    }

    public List<ItemShelf> getShelves() {
        return shelves;
    }

    public void addShelf(ItemShelf shelf) {
        shelves.add(shelf);
    }

    public ItemShelf getShelfByCode(int code) {
        for (ItemShelf shelf : shelves) {
            if (shelf.getCode() == code) {
                return shelf;
            }
        }
        return null;
    }
}
