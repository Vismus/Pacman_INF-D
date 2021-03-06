/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.strategies.ghost;

import java.util.ArrayList;

import pacman_infd.games.Cell;

/**
 *
 * @author Marinus
 */
public class FleeStrategy implements GhostStrategy {
    private PathFinder pathFinder;
    private Cell previousCell;

    public FleeStrategy(){
        pathFinder = new PathFinder();
    }

    @Override
    public Cell giveNextCell(Cell currentCell) {
        ArrayList<Cell> possibleCell = new ArrayList<>();
        Cell towardsPacman = pathFinder.nextCellInPathToPacman(currentCell);
       
        for (Cell cell : currentCell.getNeighbors().values()) {
            if(!cell.hasWall() && !cell.equals(towardsPacman) && !cell.equals(previousCell)){
                possibleCell.add(cell);
            }
        }
        
        possibleCell.add(towardsPacman);
        
        previousCell = currentCell;
        return possibleCell.get(0);
    }
}
