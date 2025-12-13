#include <iostream>
#include <stdexcept>
using namespace std;

/* ========= Forward Declarations ========= */
class ATM;
class Card;

class ATMState;
class ATMIdleState;
class ATMHasCardState;
class SelectOperationState;
class CashWithdrawalState;
class DisplayBalance;

/* ========= Transaction Type ========= */
enum class TransactionType {
    CASH_WITHDRAWAL,
    BALANCE_CHECK
};

/* ========= Card ========= */
class Card {
public:
    int balance;
    int pin;

    Card(int balance, int pin) : balance(balance), pin(pin) {}
};

/* ========= ATMState ========= */
class ATMState {
public:
    virtual void readCard(ATM*, Card*) { invalid(); }
    virtual void authenticatePin(ATM*, Card*, int) { invalid(); }
    virtual void selectOperation(ATM*, Card*, TransactionType) { invalid(); }
    virtual void cashWithdrawal(ATM*, Card*, int) { invalid(); }
    virtual void displayBalance(ATM*, Card*) { invalid(); }
    virtual void returnCard(ATM*, Card*) { invalid(); }
    virtual void exit(ATM*) { invalid(); }

    virtual ~ATMState() = default;

protected:
    [[noreturn]] void invalid() {
        throw logic_error("Invalid ATM state operation");
    }
};

/* ========= ATM ========= */
class ATM {
public:
    ATMState* atmState;
    int balance;
    int num_two_thousand;
    int num_five_hundred;
    int num_one_hundred;

    ATM(int t, int f, int o)
        : atmState(nullptr),
          num_two_thousand(t),
          num_five_hundred(f),
          num_one_hundred(o)
    {
        balance = t * 2000 + f * 500 + o * 100;
    }

    void setCurrentATMState(ATMState* s) {
        delete atmState;
        atmState = s;
    }

    ~ATM() {
        delete atmState;
    }
};

/* ========= Cash Withdrawal Chain ========= */
class CashWithdrawalProcessor {
public:
    CashWithdrawalProcessor* next;

    CashWithdrawalProcessor(CashWithdrawalProcessor* next) : next(next) {}

    virtual void withdraw(ATM* atm, int amount) {
        if (next) next->withdraw(atm, amount);
    }

    virtual ~CashWithdrawalProcessor() {
        delete next;
    }
};

class TwoThousandWithdrawalProcessor : public CashWithdrawalProcessor {
public:
    TwoThousandWithdrawalProcessor(CashWithdrawalProcessor* n)
        : CashWithdrawalProcessor(n) {}

    void withdraw(ATM* atm, int amount) override {
        int req = amount / 2000;
        int rem = amount % 2000;

        if (req > atm->num_two_thousand) {
            rem = amount - atm->num_two_thousand * 2000;
            atm->num_two_thousand = 0;

            // cout<<
        } else if(req>0) {
            atm->num_two_thousand -= req;
        }

        if (rem && next) next->withdraw(atm, rem);
    }
};

class FiveHundredWithdrawalProcessor : public CashWithdrawalProcessor {
public:
    FiveHundredWithdrawalProcessor(CashWithdrawalProcessor* n)
        : CashWithdrawalProcessor(n) {}

    void withdraw(ATM* atm, int amount) override {
        int req = amount / 500;
        int rem = amount % 500;

        if (req > atm->num_five_hundred) {
            rem = amount - atm->num_five_hundred * 500;
            atm->num_five_hundred = 0;
        } else {
            atm->num_five_hundred -= req;
        }

        if (rem && next) next->withdraw(atm, rem);
    }
};

class OneHundredWithdrawalProcessor : public CashWithdrawalProcessor {
public:
    OneHundredWithdrawalProcessor(CashWithdrawalProcessor* n)
        : CashWithdrawalProcessor(n) {}

    void withdraw(ATM* atm, int amount) override {
        int req = amount / 100;
        int rem = amount % 100;

        if (req > atm->num_one_hundred) {
            atm->num_one_hundred = 0;
        } else {
            atm->num_one_hundred -= req;
        }

        if (rem && next) next->withdraw(atm, rem);
    }
};

/* ========= State Class Declarations ========= */
class ATMIdleState : public ATMState {
public:
    void readCard(ATM*, Card*) override;
};

class ATMHasCardState : public ATMState {
public:
    void authenticatePin(ATM*, Card*, int) override;
    void returnCard(ATM*, Card*) override;
    void exit(ATM*) override;
};

class SelectOperationState : public ATMState {
public:
    void selectOperation(ATM*, Card*, TransactionType) override;
};

class CashWithdrawalState : public ATMState {
public:
    void cashWithdrawal(ATM*, Card*, int) override;
    void returnCard(ATM*, Card*) override;
};

class DisplayBalance : public ATMState {
public:
    void displayBalance(ATM*, Card*) override;
};

/* ========= State Method Definitions ========= */
void ATMIdleState::readCard(ATM* atm, Card*) {
    cout << "Reading card\n";
    atm->setCurrentATMState(new ATMHasCardState());
}

void ATMHasCardState::authenticatePin(ATM* atm, Card* c, int pin) {
    cout << "Authenticating PIN\n";
    if (c->pin != pin) {
        cout << "Invalid PIN\n";
        returnCard(atm, c);
    } else {
        atm->setCurrentATMState(new SelectOperationState());
    }
}

void ATMHasCardState::returnCard(ATM* atm, Card*) {
    exit(atm);
}

void ATMHasCardState::exit(ATM* atm) {
    atm->setCurrentATMState(new ATMIdleState());
}

void SelectOperationState::selectOperation(
    ATM* atm, Card*, TransactionType type)
{
    if (type == TransactionType::CASH_WITHDRAWAL)
    atm->setCurrentATMState(new CashWithdrawalState());
    else
    atm->setCurrentATMState(new DisplayBalance());
}

void CashWithdrawalState::cashWithdrawal(
    ATM* atm, Card* c, int amount)
{
    if (atm->balance < amount || c->balance < amount) {
        cout << "Insufficient balance\n";
        returnCard(atm, c);
        return;
    }

    cout<<"Withdrawing Cash"<<endl;
    atm->balance -= amount;
    c->balance -= amount;

    CashWithdrawalProcessor* chain =
        new TwoThousandWithdrawalProcessor(
            new FiveHundredWithdrawalProcessor(
                new OneHundredWithdrawalProcessor(nullptr)));

    chain->withdraw(atm, amount);
    delete chain;

    returnCard(atm, c);
}

void CashWithdrawalState::returnCard(ATM* atm, Card* c) {
    atm->setCurrentATMState(new ATMIdleState());
}

void DisplayBalance::displayBalance(ATM* atm, Card* c) {
    cout << "Balance: " << c->balance << "\n";
    atm->setCurrentATMState(new ATMIdleState());
}

/* ========= Main ========= */
int main() {
    ATM atm(10, 10, 10);
    atm.setCurrentATMState(new ATMIdleState());

    Card card(10000, 1234);

    atm.atmState->readCard(&atm, &card);
    atm.atmState->authenticatePin(&atm, &card, 1234);
    TransactionType choice = TransactionType::CASH_WITHDRAWAL;
    atm.atmState->selectOperation(&atm, &card, choice);

    if(choice == TransactionType::BALANCE_CHECK)
    {
        atm.atmState->displayBalance(&atm,&card);
    }
    else
    {
        atm.atmState->cashWithdrawal(&atm,&card,1000);
        atm.setCurrentATMState(new DisplayBalance());
        atm.atmState->displayBalance(&atm,&card);
    }

    return 0;
}
