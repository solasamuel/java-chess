/*
 * Abstract class for handling data on the 64 tiles on the
 * chessboard.
 */
public abstract class Tile {

    int tileCoordinate;

    Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    /*
     * Class defining any empty tiles on the chessboard.
     */
    public static final class EmptyTile extends Tile {

        EmptyTile(int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }

    /*
     * Class defining any occupied tiles on the chessboard
     * along with the piece resting in that position.
     */
    public static final class OccupiedTile extends Tile {
        Piece pieceOnTile;

        OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
