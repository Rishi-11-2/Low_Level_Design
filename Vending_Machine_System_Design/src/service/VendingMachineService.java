package service;

import model.Coin;
import model.Item;
import model.VendingMachine;
import java.util.List;

public class VendingMachineService {
    private final CoinService coinService;

    public VendingMachineService(CoinService coinService) {
        this.coinService = coinService;
    }

    public void clickInsertCoinButton(VendingMachine machine) {
        System.out.println("\n>>> [VendingMachineService] Request: Click Insert Coin Button");
        machine.clickOnInsertCoinButton();
    }

    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("\n>>> [VendingMachineService] Request: Insert Coin: " + coin.name());
        machine.insertCoin(coin);
    }

    public void startProductSelection(VendingMachine machine) {
        System.out.println("\n>>> [VendingMachineService] Request: Start Product Selection");
        machine.clickOnStartProductSelectionButton();
    }

    public void chooseProduct(VendingMachine machine, int code) {
        System.out.println("\n>>> [VendingMachineService] Request: Select Shelf Code " + code);
        try {
            machine.chooseProduct(code);
        } catch (Exception e) {
            System.out.println("[VendingMachineService] Transaction failed: " + e.getMessage());
        }
    }

    public void cancelTransaction(VendingMachine machine) {
        System.out.println("\n>>> [VendingMachineService] Request: Cancel Transaction & Refund");
        try {
            List<Coin> coins = machine.refundFullMoney();
            coinService.processRefund(coins);
        } catch (Exception e) {
            System.out.println("[VendingMachineService] Cancel failed: " + e.getMessage());
        }
    }
}
