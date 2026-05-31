package controller;

import model.Coin;
import model.VendingMachine;
import service.InventoryService;
import service.VendingMachineService;

public class VendingMachineController {
    private final VendingMachineService vendingMachineService;
    private final InventoryService inventoryService;

    public VendingMachineController(VendingMachineService vendingMachineService, InventoryService inventoryService) {
        this.vendingMachineService = vendingMachineService;
        this.inventoryService = inventoryService;
    }

    public void clickInsertCoinButton(VendingMachine machine) {
        vendingMachineService.clickInsertCoinButton(machine);
    }

    public void insertCoin(VendingMachine machine, Coin coin) {
        vendingMachineService.insertCoin(machine, coin);
    }

    public void startSelection(VendingMachine machine) {
        vendingMachineService.startProductSelection(machine);
    }

    public void selectProduct(VendingMachine machine, int code) {
        vendingMachineService.chooseProduct(machine, code);
    }

    public void cancelAndRefund(VendingMachine machine) {
        vendingMachineService.cancelTransaction(machine);
    }

    public void displayInventory() {
        inventoryService.displayInventory();
    }
}
