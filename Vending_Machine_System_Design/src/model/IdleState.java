package model;

import java.util.List;

public class IdleState implements State {

    public IdleState() {
        System.out.println("[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.");
    }

    @Override
    public void clickOnInsertCoinButton(VendingMachine machine) {
        System.out.println("[State Transition] Clicked Insert Coin Button. Transitioning to HAS_MONEY State.");
        machine.setVendingMachineState(new HasMoneyState());
    }

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        throw new IllegalStateException("Cannot insert coin in IDLE state. Click Insert Coin Button first.");
    }

    @Override
    public void clickOnStartProductSelectionButton(VendingMachine machine) {
        throw new IllegalStateException("Cannot select product in IDLE state. Insert coins first.");
    }

    @Override
    public void chooseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Cannot choose product in IDLE state. Insert coins first.");
    }

    @Override
    public int getChange(int returnChangeMoney) {
        throw new IllegalStateException("No change applicable in IDLE state.");
    }

    @Override
    public Item dispenseProduct(VendingMachine machine, int code) {
        throw new IllegalStateException("Cannot dispense in IDLE state.");
    }

    @Override
    public List<Coin> refundFullMoney(VendingMachine machine) {
        throw new IllegalStateException("No transaction to refund in IDLE state.");
    }
}
