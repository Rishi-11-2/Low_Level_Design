package model;

import java.util.List;

public interface State {
    void clickOnInsertCoinButton(VendingMachine machine);
    void insertCoin(VendingMachine machine, Coin coin);
    void clickOnStartProductSelectionButton(VendingMachine machine);
    void chooseProduct(VendingMachine machine, int code);
    int getChange(int returnChangeMoney);
    Item dispenseProduct(VendingMachine machine, int code);
    List<Coin> refundFullMoney(VendingMachine machine);
}
