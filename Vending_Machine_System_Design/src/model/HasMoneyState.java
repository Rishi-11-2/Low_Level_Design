package model;

import java.util.List;

public class HasMoneyState implements State {

    public HasMoneyState() {
        System.out.println("[State: HasMoney] Insert coins. Click 'Start Selection' when finished.");
    }

    @Override
    public void clickOnInsertCoinButton(VendingMachine machine) {
        System.out.println("[State: HasMoney] Insert Coin Button clicked again. Ready to accept more coins.");
    }

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        int total = 0;
        for (Coin c : machine.getCoinList()) {
            total += c.getValue();
        }
        System.out.println("[HasMoney] Inserted: " + coin.name() + " ($" + (coin.getValue() / 100.0) + "). Current total: $" + (total / 100.0));
    }

    @Override
    public void clickOnStartProductSelectionButton(VendingMachine machine) {
        System.out.println("[State Transition] Starting selection. Transitioning to SELECTION State.");
        machine.setVendingMachineState(new SelectionState());
    }

    @Override
    public void chooseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Cannot select product yet. Click 'Start Selection' button first.");
    }

    @Override
    public int getChange(int returnChangeMoney) {
        throw new IllegalStateException("No change applicable in HAS_MONEY state.");
    }

    @Override
    public Item dispenseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Cannot dispense in HAS_MONEY state.");
    }

    @Override
    public List<Coin> refundFullMoney(VendingMachine machine) {
        System.out.println("[Refund] Refunding full money and resetting to IDLE state.");
        List<Coin> coins = machine.refundAllCoins();
        machine.setVendingMachineState(new IdleState());
        return coins;
    }
}
