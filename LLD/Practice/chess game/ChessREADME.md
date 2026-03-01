# Chess LLD (Interview Simple)

## Implemented (only core)
- `ChessBoard` (8x8)
- `Piece` hierarchy (`King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn`)
- `ChessGame` with:
  - turn validation
  - movement validation
  - own-piece capture prevention
  - move history
  - game over when a king is captured
- `ChessMain` for console input (`e2 e4`)

## Not included (to keep it simple)
- check/checkmate/stalemate engine
- castling
- en passant
- promotion

## Class Diagram

```mermaid
classDiagram
    class ChessMain {
      +main(String[] args)
    }

    class ChessGame {
      -ChessBoard board
      -ChessPlayer whitePlayer
      -ChessPlayer blackPlayer
      -Color currentTurn
      -ChessGameStatus status
      -ChessPlayer winner
      -List~Move~ moveHistory
      +makeMove(String from, String to)
      +getBoard()
      +getCurrentPlayer()
      +getStatus()
      +getWinner()
    }

    class ChessBoard {
      -Piece[][] grid
      +getPiece(Position position)
      +setPiece(Position position, Piece piece)
      +movePiece(Position from, Position to)
      +isPathClear(Position from, Position to)
      +printBoard()
    }

    class ChessPlayer {
      -String name
      -Color color
      +getName()
      +getColor()
    }

    class Move {
      -Position from
      -Position to
      -Piece movingPiece
      -Piece capturedPiece
    }

    class Position {
      -int row
      -int col
      +fromAlgebraic(String square)
      +toAlgebraic()
    }

    class Piece {
      <<abstract>>
      -Color color
      -PieceType type
      +isValidMove(ChessBoard board, Position from, Position to)*
      +getSymbol()*
    }

    class King
    class Queen
    class Rook
    class Bishop
    class Knight
    class Pawn

    class Color {
      <<enum>>
      WHITE
      BLACK
    }

    class PieceType {
      <<enum>>
      KING
      QUEEN
      ROOK
      BISHOP
      KNIGHT
      PAWN
    }

    class ChessGameStatus {
      <<enum>>
      IN_PROGRESS
      WHITE_WIN
      BLACK_WIN
    }

    class ChessInvalidMoveException {
      <<exception>>
    }

    ChessMain --> ChessGame : creates
    ChessGame o-- ChessBoard
    ChessGame o-- ChessPlayer
    ChessGame o-- Move
    Move o-- Position
    Move o-- Piece
    ChessBoard o-- Piece
    Piece <|-- King
    Piece <|-- Queen
    Piece <|-- Rook
    Piece <|-- Bishop
    Piece <|-- Knight
    Piece <|-- Pawn
    Piece ..> Color
    Piece ..> PieceType
    ChessGame ..> ChessGameStatus
    ChessGame ..> ChessInvalidMoveException : throws
```

## Run
From this folder:

```bash
javac *.java
java -cp . ChessMain
```

If you run from project root (`D:\Projects\Upskilling`), use:

```bash
cd "LLD\Practice\chess game"
javac *.java
java -cp . ChessMain
```
