package group_0617.csc207.gamecentre;

import android.support.annotation.NonNull;

import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board.
 * Which implement Iterable<Tile> and Serializable.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * The number of rows and columns.
     */
    private int complexity = 4;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[complexity][complexity];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == this.complexity * this.complexity
     *
     * @param tiles the tiles for the board
     */
    Board(List<Tile> tiles) {
        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != this.complexity; row++) {
            for (int col = 0; col != this.complexity; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        return this.complexity * this.complexity;
    }

    /**
     * Return the complexity of the board
     * @return the complexity of the board
     */
    int getComplexity() {
        return this.complexity;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile original_tile = getTile(row1, col1);
        tiles[row1][col1] = getTile(row2, col2);
        tiles[row2][col2] = original_tile;
        setChanged();
        notifyObservers();
    }

    /**
     * Count the number of inversion for current tile specified by row and column number
     * @param row the row of current tile
     * @param col the column of current tile
     * @return the number of inversion for current tile
     */
    private int countInversion(int row, int col){
        int inv = 0;
        int pos = row * this.complexity + col;
        int posToCompare = pos+1;
        Tile currentTile = getTile(row, col);
        Tile tileToCompare;
        while (posToCompare < numTiles()) {
            tileToCompare = getTile(posToCompare/this.complexity, posToCompare%this.complexity);
            if (tileToCompare.getId() != numTiles()) {
                if (currentTile.getId() > tileToCompare.getId()) {
                    inv++;
                }
            }
            posToCompare++;
        }

        return inv;
    }

    /**
     * Return the sum of polarity over all tiles in the blank.
     *
     * @return the sum of polarity over all tiles in the blank.
     */
    private int sumOverPolarity() {

        int totInv = 0;
        for (int i = 0; i < this.complexity; i++) {
            for (int j = 0; j < this.complexity; j++) {
                if (getTile(i, j).getId() != numTiles()) {
                    totInv = totInv + countInversion(i, j);
                }
            }
        }
        return totInv;
    }

    /**
     * Check if the board is solvable
     * @return if the board is solvable
     */
    public boolean isSolvable() {
        boolean isEvenPol = sumOverPolarity()%2 == 0;
        return this.complexity%2 == 1 && isEvenPol || this.complexity%2 == 0 && blankOnOddRowFromBottom() == isEvenPol;
    }

    /**
     * This method check if the board is solvable. If not, it makes the board solvable
     */
    public void makeSolvable() {
        if (!isSolvable()) {
            if (getTile(0, 0).getId() == numTiles() || getTile(1, 0).getId() == numTiles()) {
                swapTiles(this.complexity-1, this.complexity-1, this.complexity-1, this.complexity-2);
            } else {
                swapTiles(0, 0, 1, 0);
            }
        }
    }

    /**
     * Check if blank tile is on odd row counting from bottom
     * @return if blank tile is on odd row counting from bottom
     */
    private boolean blankOnOddRowFromBottom() {
        boolean re = false;
        for (int row = this.complexity -1; row >= 0; row++) {
            if ((this.complexity - row) % 2 == 1) {
                for (int col = 0; col < this.complexity; col++) {
                    if (getTile(row, col).getId() == numTiles()) {
                        re = true;
                    }
                }
            }
        }
        return re;
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    private class BoardIterator implements Iterator<Tile> {
        int rowIndex = 0;
        int columnIndex = 0;

        @Override
        public boolean hasNext() {
            return rowIndex != complexity - 1 ||
                    columnIndex != complexity - 1;
        }

        @Override
        public Tile next() {
            Tile result = tiles[rowIndex][columnIndex];
            if (rowIndex != complexity - 1) {
                if (columnIndex != complexity - 1) {
                    columnIndex++;
                } else {
                    rowIndex++;
                    columnIndex = 0;
                }
            } else {
                if (columnIndex != complexity - 1) {
                    columnIndex++;
                }
            }
            return result;
        }

    }

}