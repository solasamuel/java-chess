package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

/*
 * Class describing & implementing all types of piece move.
 */
public abstract class Move {

    final Board board;
    final Piece piece;
    final int destinationCoordinate;

    private Move(final Board board,
                 final Piece piece,
                 final int destinationCoordinate) {
        this.board = board;
        this.piece = piece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public Board execute() {
        return null;
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public static final class RegularMove extends Move {

        public RegularMove(final Board board,
                           final Piece piece,
                           final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    public static final class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }
}
