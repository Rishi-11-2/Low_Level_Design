package model;

import java.util.List;

public class SelectionState implements State {

    public SelectionState() {
        System.out.println("[State: Selection] Select a product by shelf code.");
    }

    @Override
    public void clickOnInsertCoinButton(VendingMachine machine) {
        throw new IllegalStateException("Cannot insert coin in SELECTION state.");
    }

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        throw new IllegalStateException("Cannot insert coin in SELECTION state.");
    }

    @Override
    public void clickOnStartProductSelectionButton(VendingMachine machine) {
        System.out.println("[State: Selection] Already in selection state.");
    }

    @Override
    public void chooseProduct(VendingMachine machine, int code) {
        ItemShelf shelf = machine.getInventory().getShelfByCode(code);
        if (shelf == null) {
            System.out.println("[SelectionError] Shelf code " + code + " does not exist.");
            refundFullMoney(machine);
            throw new IllegalArgumentException("Shelf code " + code + " is invalid.");
        }

        if (shelf.isSoldOut()) {
            System.out.println("[SelectionError] Product in shelf " + code + " is sold out.");
            refundFullMoney(machine);
            throw new IllegalStateException("Product is sold out.");
        }

        int balance = 0;
        for (Coin coin : machine.getCoinList()) {
            balance += coin.getValue();
        }

        Item item = shelf.getItem();
        if (balance < item.getPrice()) {
            System.out.println("[SelectionError] Insufficient balance: Required $" + (item.getPrice() / 100.0) + ", but inserted $" + (balance / 100.0));
            refundFullMoney(machine);
            throw new IllegalStateException("Insufficient money inserted.");
        }

        System.out.println("[Selection] Successfully chosen: " + item.getType().name() + " ($" + (item.getPrice() / 100.0) + ")");
        int change = balance - item.getPrice();
        if (change > 0) {
            getChange(change);
        }

        // Transition to DispenseState
        machine.setVendingMachineState(new DispenseState(code));
        machine.dispenseProduct(code);
    }

    @Override
    public int getChange(int returnChangeMoney) {
        System.out.println("[ChangeReturn] Dispensed exact change back to user: $" + (returnChangeMoney / 100.0));
        return returnChangeMoney;
    }

    @Override
    public Item dispenseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Select product before dispensing.");
    }

    @Override
    public List<Coin> refundFullMoney(VendingMachine machine) {
        System.out.println("[Refund] Returning all inserted money and returning to IDLE state.");
        List<Coin> coins = machine.refundAllCoins();
        machine.setVendingMachineState(new IdleState());
        return coins;
    }
}
