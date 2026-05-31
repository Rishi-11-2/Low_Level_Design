package service;

import model.Inventory;
import model.Item;
import model.ItemShelf;
import model.ItemType;
import repository.InventoryRepository;

public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void addShelf(int code, ItemType type, int price, int quantity) {
        Item item = new Item(type, price);
        ItemShelf shelf = new ItemShelf(code, item, quantity);
        inventoryRepository.get().addShelf(shelf);
        System.out.println("[InventoryService] Added shelf " + code + " for " + type.name() + " @ $" + (price / 100.0) + " (Qty: " + quantity + ")");
    }

    public ItemShelf getShelf(int code) {
        return inventoryRepository.getShelf(code);
    }

    public void displayInventory() {
        System.out.println("\n============ VENDING MACHINE INVENTORY ============");
        for (ItemShelf shelf : inventoryRepository.get().getShelves()) {
            String status = shelf.isSoldOut() ? "SOLD_OUT" : "QTY: " + shelf.getQuantity();
            System.out.printf("Shelf Code: %-5d | Product: %-8s | Price: $%-5.2f | Status: %s\n",
                    shelf.getCode(), shelf.getItem().getType().name(), (shelf.getItem().getPrice() / 100.0), status);
        }
        System.out.println("====================================================");
    }
}
