package model;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
    private Inventory inventory;
    private State state;
    private final List<Coin> coinList;

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.coinList = new ArrayList<>();
    }

    public State getState() {
        return state;
    }

    public void setVendingMachineState(State state) {
        this.state = state;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Coin> getCoinList() {
        return coinList;
    }

    public void addCoin(Coin coin) {
        this.coinList.add(coin);
    }

    public List<Coin> refundAllCoins() {
        List<Coin> refund = new ArrayList<>(coinList);
        coinList.clear();
        return refund;
    }

    public void clickOnInsertCoinButton() {
        state.clickOnInsertCoinButton(this);
    }

    public void insertCoin(Coin coin) {
        state.insertCoin(this, coin);
    }

    public void clickOnStartProductSelectionButton() {
        state.clickOnStartProductSelectionButton(this);
    }

    public void chooseProduct(int code) {
        state.chooseProduct(this, code);
    }

    public Item dispenseProduct(int code) {
        return state.dispenseProduct(this, code);
    }

    public List<Coin> refundFullMoney() {
        return state.refundFullMoney(this);
    }
}
