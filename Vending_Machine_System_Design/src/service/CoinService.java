package service;

import model.Coin;
import java.util.List;

public class CoinService {

    public int getBalance(List<Coin> coins) {
        int balance = 0;
        for (Coin c : coins) {
            balance += c.getValue();
        }
        return balance;
    }

    public boolean isSufficient(int balance, int price) {
        return balance >= price;
    }

    public void processRefund(List<Coin> coins) {
        int totalRefunded = getBalance(coins);
        System.out.println("[CoinService] Refunding " + coins.size() + " coins back to user. Sum: $" + (totalRefunded / 100.0));
    }
}
