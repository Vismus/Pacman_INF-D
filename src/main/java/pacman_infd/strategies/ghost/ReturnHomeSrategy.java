/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.strategies.ghost;

import pacman_infd.games.Cell;

import java.util.List;

/**
 *
 * @author Marinus
 */
public class ReturnHomeSrategy implements GhostStrategy {
    private PathFinder pathFinder;
    private Cell homeCell;
    
    public ReturnHomeSrategy(Cell homeCell){
        pathFinder = new PathFinder();
        this.homeCell = homeCell;
    }
    
    @Override
    public Cell giveNextCell(Cell currentCell) {
        List<Cell> homePath = pathFinder.breathFirstSearch(currentCell, homeCell);
        if(homePath.isEmpty()) {
            return null;
        } else {
            return homePath.get(0);
        }
    }
}
