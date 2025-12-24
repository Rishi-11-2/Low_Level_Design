#include <iostream>
#include <string>
#include <vector>
#include <deque>
#include <queue>
#include <map>
#include <memory>

using namespace std;

// Forward declarations
class Ball;
class Innings;
class ScoreUpdaterObserver;
class Team;
class Player;

// Enums
enum class PlayerType {
    BATTER,
    BOWLER,
    ALLROUNDER,
    WICKET_KEEPER,
    CAPTAIN
};

enum class BowlType {
    NOBALL,
    WIDEBALL,
    NORMALBALL,
    OUT
};

// Simple person
class Person {
public:
    string name;
    string address;
    int age;

    Person(const string& name, const string& address, int age)
        : name(name), address(address), age(age) {}
};

// Scorecards
class BattingScoreCard {
public:
    int totalRuns = 0;
    int ballsPlayed = 0;
    int totalFours = 0;
    int totalSixes = 0;
    double strikeRate() const {
        if (ballsPlayed == 0) return 0.0;
        return (double)totalRuns / ballsPlayed * 100.0;
    }
};

class BowlingScoreCard {
public:
    int totalBallsDelivered = 0;
    int runsGiven = 0;
    int wicketsTaken = 0;
    double economyRate() const {
        if (totalBallsDelivered == 0) return 0.0;
        double overs = totalBallsDelivered / 6.0;
        if (overs == 0.0) return 0.0;
        return runsGiven / overs;
    }
};

// Player
class Player {
public:
    Person* person;
    PlayerType type;
    BattingScoreCard* bac = nullptr;
    BowlingScoreCard* boc = nullptr;

    Player(Person* p, PlayerType t) : person(p), type(t) {
        // allocate relevant scorecards
        bac = new BattingScoreCard();
        boc = new BowlingScoreCard();
    }

    ~Player() {
        delete bac;
        delete boc;
    }

    string name() const { return person ? person->name : string("Unknown"); }
};

// Observer pattern for score updates
class ScoreUpdaterObserver {
public:
    virtual ~ScoreUpdaterObserver() = default;
    virtual void update(Ball* ball) = 0;
};

// Basic concrete updaters (skeleton)
class BattingScoreCardUpdater : public ScoreUpdaterObserver {
public:
    void update(Ball* ball) override;
};

class BowlingScoreCardUpdater : public ScoreUpdaterObserver {
public:
    void update(Ball* ball) override;
};

// Ball (one delivery)
class Ball {
public:
    int ballNumber = 0;
    BowlType bType = BowlType::NORMALBALL;
    int runs = 0;
    Player* onStrike = nullptr;
    Player* bowler = nullptr;
    Innings* innings = nullptr;

    Ball(Innings* inn, int number) : innings(inn), ballNumber(number) {}

    void startBallDeliver(); // implement later
    void notify();          // will call innings->notifyBall(this)
};

// Over (6 legal deliveries — simplified, ignoring extras handling)
class Over {
public:
    int overNumber = 0;
    vector<Ball*> balls;
    Innings* innings = nullptr;
    Player* bowler = nullptr;

    Over(Innings* inn, int overNo, Player* bowlerPtr = nullptr)
        : innings(inn), overNumber(overNo), bowler(bowlerPtr) {}

    ~Over() {
        for (Ball* b : balls) delete b;
    }

    // create up to 6 deliveries (this is simplified and treats all as legal)
    void startOver();
};

// Innings
class Innings {
public:
    vector<ScoreUpdaterObserver*> observers;
    Team* battingTeam = nullptr;
    Team* bowlingTeam = nullptr;
    vector<Over*> overs;
    int totalRuns = 0;
    int wickets = 0;

    ~Innings() {
        for (Over* o : overs) delete o;
    }

    void addObserver(ScoreUpdaterObserver* obs) {
        observers.push_back(obs);
    }

    void notifyBall(Ball* ball) {
        // notify all observers
        for (auto* obs : observers) {
            if (obs) obs->update(ball);
        }
    }

    // convenience for Ball to call
    void notify(Ball* ball) { notifyBall(ball); }
};

// Match type base and derived types
class Matchtype {
protected:
    int overs;
    int maximumOversAllowed;
    int days;

public:
    Matchtype(int overs, int maximumOversAllowed, int days = 1)
        : overs(overs), maximumOversAllowed(maximumOversAllowed), days(days) {}

    virtual ~Matchtype() = default;
    virtual int getOvers() const = 0;
    virtual int getMaximumOversAllowed() const = 0;
};

class ODI : public Matchtype {
public:
    ODI() : Matchtype(50, 10) {}
    int getOvers() const override { return overs; }
    int getMaximumOversAllowed() const override { return maximumOversAllowed; }
};

class T20 : public Matchtype {
public:
    T20() : Matchtype(20, 5) {}
    int getOvers() const override { return overs; }
    int getMaximumOversAllowed() const override { return maximumOversAllowed; }
};

class Test : public Matchtype {
public:
    Test() : Matchtype(90, 20, 5) {}
    int getOvers() const override { return overs; }
    int getMaximumOversAllowed() const override { return maximumOversAllowed; }
};

// Bowling and batting controllers (simple)
class PlayerBattingController {
public:
    queue<Player*> yetToPlay;
    Player* striker = nullptr;
    Player* nonStriker = nullptr;
};

class PlayerBowlingController {
public:
    deque<Player*> bowlers;
    map<Player*, int> overCount;
    Player* currentBowler = nullptr;
};

// Team
class Team {
public:
    string name;
    deque<Player*> playing11;
    deque<Player*> bench;
    PlayerBattingController* battingController = nullptr;
    PlayerBowlingController* bowlingController = nullptr;

    Team(const string& n) : name(n) {
        battingController = new PlayerBattingController();
        bowlingController = new PlayerBowlingController();
    }
    ~Team() {
        // free players owned by team
        for (Player* p : playing11) delete p;
        for (Player* p : bench) delete p;
        delete battingController;
        delete bowlingController;
    }

    void addPlayerToPlaying(Player* p) { playing11.push_back(p); }
    void addPlayerToBench(Player* p) { bench.push_back(p); }
};

// Match
class Match {
public:
    Team* a = nullptr;
    Team* b = nullptr;
    string venue;
    string date;
    vector<Innings*> inningsList;
    Matchtype* matchType = nullptr;
    int tossWinner = -1; // 0 -> team a, 1 -> team b

    // Match owns teams and matchType (for simplicity)
    Match(Team* a_, Team* b_, const string& venue_, const string& date_, Matchtype* mtype)
        : a(a_), b(b_), venue(venue_), date(date_), matchType(mtype) {}

    ~Match() {
        for (Innings* inn : inningsList) delete inn;
        delete matchType;
        delete a;
        delete b;
    }

    void startMatch();
};

// Implementations
void Ball::startBallDeliver() {
    // simple example: random run 0..6 (deterministic here for example)
    // In real sim, decide bType, runs etc.
    runs = 1; // for sample, each ball yields 1 run
    if (onStrike && innings) {
        innings->totalRuns += runs;
        // update player's batting card
        if (onStrike->bac) {
            onStrike->bac->totalRuns += runs;
            onStrike->bac->ballsPlayed += 1;
        }
        // update bowler's bowling card
        if (bowler && bowler->boc) {
            bowler->boc->totalBallsDelivered += 1;
            bowler->boc->runsGiven += runs;
        }
    }
    notify();
}

void Ball::notify() {
    if (innings) innings->notify(this);
}

void Over::startOver() {
    // create 6 balls (simplified — does not handle wides/no-balls)
    for (int i = 1; i <= 6; ++i) {
        Ball* b = new Ball(innings, (overNumber - 1) * 6 + i);
        b->bowler = bowler;
        // set onStrike from batting team controller if available
        if (innings && innings->battingTeam && innings->battingTeam->battingController)
            b->onStrike = innings->battingTeam->battingController->striker;
        balls.push_back(b);
        b->startBallDeliver();
    }
}

// Simple updater implementations (demonstrative)
void BattingScoreCardUpdater::update(Ball* ball) {
    // For demonstration, print a small message
    if (!ball || !ball->onStrike) return;
    cout << "[BattingUpdater] " << ball->onStrike->name()
         << " scored " << ball->runs << " on ball " << ball->ballNumber << "\n";
}

void BowlingScoreCardUpdater::update(Ball* ball) {
    if (!ball || !ball->bowler) return;
    cout << "[BowlingUpdater] " << ball->bowler->name()
         << " conceded " << ball->runs << " on ball " << ball->ballNumber << "\n";
}

void Match::startMatch() {
    cout << "Starting match at " << venue << " on " << date << "\n";
    // Simple toss simulation: team a bats first (for determinism)
    tossWinner = 0;
    // create first innings
    Innings* inn1 = new Innings();
    inn1->battingTeam = a;
    inn1->bowlingTeam = b;

    // attach updaters
    inn1->addObserver(new BattingScoreCardUpdater());
    inn1->addObserver(new BowlingScoreCardUpdater());

    // set striker if available
    if (!a->playing11.empty()) {
        inn1->battingTeam->battingController->striker = a->playing11.front();
        // next batsman
        for (size_t i = 1; i < a->playing11.size(); ++i)
            inn1->battingTeam->battingController->yetToPlay.push(a->playing11[i]);
    }

    // pick a bowler if available
    if (!b->playing11.empty())
        inn1->bowlingTeam->bowlingController->currentBowler = b->playing11.front();

    // simulate overs equal to matchType->getOvers()
    int totalOvers = matchType ? matchType->getOvers() : 0;
    for (int o = 1; o <= totalOvers; ++o) {
        Over* over = new Over(inn1, o, inn1->bowlingTeam->playing11.empty() ? nullptr : inn1->bowlingTeam->playing11.front());
        inn1->overs.push_back(over);
        over->startOver();
    }

    inningsList.push_back(inn1);

    // Print innings summary
    cout << "Innings 1 total runs: " << inn1->totalRuns << "\n";
}

// Example main to demonstrate usage
int main() {
    // create persons and players
    Person* p1 = new Person("Rohit Sharma", "Mumbai", 37);
    Person* p2 = new Person("Jasprit Bumrah", "Ahmedabad", 31);

    Player* player1 = new Player(p1, PlayerType::BATTER);
    Player* player2 = new Player(p2, PlayerType::BOWLER);

    // create teams
    Team* teamA = new Team("India");
    teamA->addPlayerToPlaying(player1); // team takes ownership of player
    // add some filler players so loops don't break
    for (int i = 0; i < 10; ++i) {
        Person* temp = new Person("PlayerA" + to_string(i + 2), "City", 25 + i);
        teamA->addPlayerToPlaying(new Player(temp, PlayerType::ALLROUNDER));
    }

    Team* teamB = new Team("Australia");
    teamB->addPlayerToPlaying(player2);
    for (int i = 0; i < 10; ++i) {
        Person* temp = new Person("PlayerB" + to_string(i + 2), "City", 24 + i);
        teamB->addPlayerToPlaying(new Player(temp, PlayerType::ALLROUNDER));
    }

    // create match
    Matchtype* mtype = new T20();
    Match match(teamA, teamB, "Mumbai Stadium", "2025-12-23", mtype);

    match.startMatch();

    // cleanup: Match destructor will delete teams and matchType and innings
    return 0;
}
