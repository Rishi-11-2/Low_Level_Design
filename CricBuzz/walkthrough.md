# Cricbuzz System LLD Java Implementation Walkthrough

We have successfully implemented and verified the Low-Level Design (LLD) for the Cricbuzz Cricket Scoring Platform in Java under `/Users/rishi/Projects/LLD/CricBuzz/src/`. The design strictly follows the tiered enterprise architecture (Client -> Service -> Domain) and incorporates the Observer and Strategy design patterns as defined in `Cricbuzz.cpp`.

---

## 1. Package Structure Created
All source files are organized under `/Users/rishi/Projects/LLD/CricBuzz/src/`:

```
src/
├── Main.java                        (System bootstrap & simulation runner)
├── model/
│   ├── PlayerType.java              (Enum: BATTER, BOWLER, ALLROUNDER, WICKET_KEEPER, CAPTAIN)
│   ├── BowlType.java                (Enum: NORMALBALL, OUT, NOBALL, WIDEBALL)
│   ├── Person.java                  (Domain model representing standard info)
│   ├── Player.java                  (Domain model holding scorecards and person info)
│   ├── BattingScoreCard.java        (Domain model storing batting stats)
│   ├── BowlingScoreCard.java        (Domain model storing bowling stats)
│   ├── Ball.java                    (Domain model representing one delivery)
│   ├── Over.java                    (Domain model representing overs of balls)
│   ├── Innings.java                 (Domain model acting as observer publisher)
│   ├── ScoreUpdaterObserver.java    (Observer Pattern: observer interface)
│   ├── BattingScoreCardUpdater.java (Observer Pattern: concrete scorecard listener)
│   ├── BowlingScoreCardUpdater.java (Observer Pattern: concrete scorecard listener)
│   ├── Matchtype.java               (Strategy Pattern: match format base class)
│   ├── T20.java                     (Strategy Pattern: concrete T20 format)
│   ├── ODI.java                     (Strategy Pattern: concrete ODI format)
│   ├── Test.java                    (Strategy Pattern: concrete Test format)
│   ├── PlayerBattingController.java (Batting lineup queue tracker)
│   ├── PlayerBowlingController.java (Bowling lineup queue tracker)
│   ├── Team.java                    (Domain model representing a squad)
│   └── Match.java                   (Domain model representing match parameters)
└── service/
    └── MatchService.java            (Coordinates matches, innings simulations, and prints score summaries)
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **Observer Design Pattern (Real-time score updates)**:
   - `Innings` holds the list of observers (`ScoreUpdaterObserver`).
   - `BattingScoreCardUpdater` and `BowlingScoreCardUpdater` act as concrete observers, listening to `Innings` delivery events and automatically updating respective player cards in real-time.
2. **Strategy Design Pattern (Match Formats)**:
   - Different formats (T20, ODI, Test) vary in overs, sessions, and limits, cleanly managed via the pluggable `Matchtype` strategy format subclasses.
3. ** line-up Controllers**:
   - `PlayerBattingController` handles striker-swaps on odd runs and wicket-falls, popping next batsmen out of the `yetToPlay` queue.

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/service/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
==============================================================
          CRICBUZZ LOW LEVEL DESIGN SYSTEM BOOT               
==============================================================

--- Setup: Initializing Teams and Squads ---
[MatchService] Starting match at Wankhede Stadium, Mumbai on 2026-05-31
[MatchService] Format:  (2 overs)
[MatchService] Team 'India' won the toss and chose to BAT first.

--- Innings 1 Start: India batting ---

--- Over 1 (Bowler: Mitchell Starc) ---
[BattingObserver] Rohit Sharma scored 4 runs on ball 1
[BowlingObserver] Mitchell Starc conceded 4 runs on ball 1
[BattingObserver] Rohit Sharma scored 1 runs on ball 2
[BowlingObserver] Mitchell Starc conceded 1 runs on ball 2
[BattingObserver] Virat Kohli scored 1 runs on ball 3
[BowlingObserver] Mitchell Starc conceded 1 runs on ball 3
[BattingObserver] Rohit Sharma scored 6 runs on ball 4
[BowlingObserver] Mitchell Starc conceded 6 runs on ball 4
[BattingObserver] Rohit Sharma scored 1 runs on ball 5
[BowlingObserver] Mitchell Starc conceded 1 runs on ball 5
[BattingObserver] Virat Kohli scored 1 runs on ball 6
[BowlingObserver] Mitchell Starc conceded 1 runs on ball 6

--- Over 2 (Bowler: Pat Cummins) ---
[BattingObserver] Virat Kohli scored 1 runs on ball 7
[BowlingObserver] Pat Cummins conceded 1 runs on ball 7
[BattingObserver] Rohit Sharma scored 0 runs on ball 8
[BowlingObserver] Pat Cummins conceded 0 runs on ball 8
[Wicket] Rohit Sharma is OUT! Next batter in: Jasprit Bumrah
[BattingObserver] Jasprit Bumrah scored 2 runs on ball 9
[BowlingObserver] Pat Cummins conceded 2 runs on ball 9
[BattingObserver] Jasprit Bumrah scored 4 runs on ball 10
[BowlingObserver] Pat Cummins conceded 4 runs on ball 10
[BattingObserver] Jasprit Bumrah scored 1 runs on ball 11
[BowlingObserver] Pat Cummins conceded 1 runs on ball 11
[BattingObserver] Virat Kohli scored 1 runs on ball 12
[BowlingObserver] Pat Cummins conceded 1 runs on ball 12

==============================================================
   INNINGS 1 SUMMARY: India 23/1
==============================================================

==============================================================
               CRICBUZZ FINAL SCORECARD                       
==============================================================
India Batting Card:
  Rohit Sharma    | Runs: 12  | Balls: 5   | 4s: 1  | 6s: 1  | SR: 240.00
  Virat Kohli     | Runs: 4   | Balls: 4   | 4s: 0  | 6s: 0  | SR: 100.00
  Jasprit Bumrah  | Runs: 7   | Balls: 3   | 4s: 1  | 6s: 0  | SR: 233.33

Australia Bowling Card:
  Mitchell Starc  | Balls: 6   | Runs: 14  | Wickets: 0  | Econ: 14.00
  Pat Cummins     | Balls: 6   | Runs: 9   | Wickets: 1  | Econ: 9.00 

==============================================================
          CRICBUZZ SYSTEM SIMULATION COMPLETE                 
==============================================================
```
