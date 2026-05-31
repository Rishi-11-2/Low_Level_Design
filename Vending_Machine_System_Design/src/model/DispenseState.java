package model;

import java.util.List;

public class DispenseState implements State {
    private final int selectedCode;

    public DispenseState(int selectedCode) {
        this.selectedCode = selectedCode;
        System.out.println("[State: Dispense] Dispensing product for shelf code: " + selectedCode);
    }

    @Override
    public void clickOnInsertCoinButton(VendingMachine machine) {
        throw new IllegalStateException("Currently dispensing product. Cannot insert coins.");
    }

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        throw new IllegalStateException("Currently dispensing product. Cannot insert coins.");
    }

    @Override
    public void clickOnStartProductSelectionButton(VendingMachine machine) {
        throw new IllegalStateException("Currently dispensing product. Cannot select product.");
    }

    @Override
    public void chooseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Currently dispensing product. Selection already finalized.");
    }

    @Override
    public int getChange(int returnChangeMoney) {
        throw new IllegalStateException("Change already processed in selection state.");
    }

    @Override
    public Item dispenseProduct(VendingMachine machine, int code) {
        ItemShelf shelf = machine.getInventory().getShelfByCode(selectedCode);
        if (shelf == null || shelf.isSoldOut()) {
            throw new IllegalStateException("Dispensation error: shelf state corrupted.");
        }

        shelf.decreaseQuantity();
        Item item = shelf.getItem();
        System.out.println("[Dispenser] Dispensed: " + item.getType().name() + " from shelf " + selectedCode);

        machine.refundAllCoins(); 
        machine.setVendingMachineState(new IdleState());

        return item;
    }

    @Override
    public List<Coin> refundFullMoney(VendingMachine machine) {
        throw new IllegalStateException("Product already purchased. Cannot refund.");
    }
}
