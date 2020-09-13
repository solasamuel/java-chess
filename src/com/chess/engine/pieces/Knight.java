package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

/*
 * Class describing the KinightPiece and it's properties.
 */
public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate;
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusive(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusive(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusive(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusive(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new RegularMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusive(final int piecePosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[piecePosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusive(final int piecePosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[piecePosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusive(final int piecePosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[piecePosition] && (candidateOffset == 10 || candidateOffset == -6);
    }

    private static boolean isEighthColumnExclusive(final int piecePosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[piecePosition] && (candidateOffset == 17 || candidateOffset == 10 ||
                candidateOffset == -6 || candidateOffset == -15);
    }


}
