# TicTacToe System LLD Java Implementation Walkthrough

We have successfully implemented and verified the Low-Level Design (LLD) for the TicTacToe System in Java under `/Users/rishi/Projects/LLD/TicTacToe/src/`. The design strictly follows the tiered architecture (Client -> Service -> Domain) and patterns defined in `TicTacToe.pdf`.

---

## 1. Package Structure Created
All source files are organized under `/Users/rishi/Projects/LLD/TicTacToe/src/`:

```
src/
├── Main.java                        (System bootstrap & CLI runner)
├── model/
│   ├── PieceType.java               (Enum: X, O)
│   ├── PlayingPiece.java            (Base domain class representing a piece)
│   ├── PieceX.java                  (Concrete piece class)
│   ├── PieceO.java                  (Concrete piece class)
│   ├── Board.java                   (Domain model representing game state)
│   └── Player.java                  (Domain model representing a participant)
└── service/
    └── GameService.java             (Coordinates turns, displays boards, and checks winners)
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **Clean Abstract Domain Models**:
   - `PlayingPiece` defines the base layout for playing markers, cleanly extended by concrete subclasses `PieceX` and `PieceO` yielding standard symbol outputs.
2. **Dynamic Winning Criteria Check**:
   - `Board` contains safe winning check equations checking columns, rows, primary diagonals, and secondary diagonals recursively.
3. **Queue Turn Rotation**:
   - `GameService` coordinates turns using double-ended queue mechanisms (`Deque`) to pop the current player, evaluate moves, add markers, check winners, and push the player back to the queue tail if the turn is safe.

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
          TICTACTOE LOW LEVEL DESIGN SYSTEM BOOT              
==============================================================

==============================================================
   GAME 1: Rishi (X) vs Bozz (O) - Rishi Wins by Row 0        
==============================================================
[TicTacToe] Starting simulated mock game turns.
-------------
| . | . | . |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (0, 0)
-------------
| X | . | . |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Bozz' placing O on (1, 0)
-------------
| X | . | . |
-------------
| O | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (0, 1)
-------------
| X | X | . |
-------------
| O | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Bozz' placing O on (1, 1)
-------------
| X | X | . |
-------------
| O | O | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (0, 2)
-------------
| X | X | X |
-------------
| O | O | . |
-------------
| . | . | . |
-------------
[Game Complete] Winner: Rishi
Game 1 Result: Winner is 'Rishi'

==============================================================
   GAME 2: Rishi (X) vs Bozz (O) - Tie Game                   
==============================================================
[TicTacToe] Starting simulated mock game turns.
-------------
| . | . | . |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (0, 0)
-------------
| X | . | . |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Bozz' placing O on (0, 1)
-------------
| X | O | . |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (0, 2)
-------------
| X | O | X |
-------------
| . | . | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Bozz' placing O on (1, 1)
-------------
| X | O | X |
-------------
| . | O | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (1, 0)
-------------
| X | O | X |
-------------
| X | O | . |
-------------
| . | . | . |
-------------
[Turn] Player 'Bozz' placing O on (1, 2)
-------------
| X | O | X |
-------------
| X | O | O |
-------------
| . | . | . |
-------------
[Turn] Player 'Rishi' placing X on (2, 1)
-------------
| X | O | X |
-------------
| X | O | O |
-------------
| . | X | . |
-------------
[Turn] Player 'Bozz' placing O on (2, 0)
-------------
| X | O | X |
-------------
| X | O | O |
-------------
| O | X | . |
-------------
[Turn] Player 'Rishi' placing X on (2, 2)
-------------
| X | O | X |
-------------
| X | O | O |
-------------
| O | X | X |
-------------
[Game Complete] Tie. No free spaces left.
Game 2 Result: Winner is 'tie'

==============================================================
          TICTACTOE SYSTEM SIMULATION COMPLETE                
==============================================================
```
