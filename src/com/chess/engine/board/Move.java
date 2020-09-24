package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

/*
 * Class describing & implementing all types of piece move.
 */
public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private static final Move NULL_MOVE = new NullMove();

    private Move(final Board board,
                 final Piece piece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getCurrentDestination() {
        return this.getMovedPiece().getPiecePosition();
    }

    public Board execute() {

        final Builder builder = new Builder();

        // set active pieces
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                // TODO: hashcode && equals for piece
                builder.setPiece(piece);
            }
        }

        // set opponent pieces
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        // set moved piece and change moveMaker
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public boolean isAttackMove() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.getDestinationCoordinate();
        result = prime * result + this.getMovedPiece().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                this.getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public boolean isAttack() {
        return false;
    }

    public static final class RegularMove extends Move {
        public RegularMove(final Board board,
                           final Piece piece,
                           final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && this.getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    public static final class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece piece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends AttackMove {
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece piece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();
            // set other active pieces
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!(this.movedPiece.equals(piece))) {
                    builder.setPiece(piece);
                }
            }
            // set opponent piece
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // set moved Pawn, EnPassant Pawn & change moveMoker
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        private CastleMove(final Board board,
                           final Piece piece,
                           final int destinationCoordinate,
                           final Rook castleRook,
                           final int castleRookStart,
                           final int castleRookDestination) {
            super(board, piece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            // set active pieces
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!(this.movedPiece.equals(piece) && this.castleRook.equals(piece))) {
                    // TODO: hashcode && equals for piece
                    builder.setPiece(piece);
                }
            }
            // set opponent pieces
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // set moved piece and change moveMaker
            builder.setPiece(this.movedPiece.movePiece(this));
            // TODO: look into first move on normal pieces
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute a null move!");
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Cannot Instantiate MoveFactory.");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {

            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentDestination() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }

            return NULL_MOVE;
        }
    }
}
