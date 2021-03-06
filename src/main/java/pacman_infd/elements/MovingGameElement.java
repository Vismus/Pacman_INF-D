/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import pacman_infd.games.Cell;
import pacman_infd.listeners.ElementEventListener;

/**
 *
 * @author Marinus
 */
public abstract class MovingGameElement extends GameElement{
    protected Cell startCell;
    private Timer timer;
    protected int speed;

    public MovingGameElement(Cell cell, ElementEventListener gameEventListener, int speed) {
        this.cell = cell;
        this.elementEventListener = gameEventListener;
        cell.addMovingElement(this);
        startCell = cell;
        this.speed = speed;

        ActionListener moveTimerActionListener = this::moveTimerActionPerformed;
        
        timer = new Timer(speed, moveTimerActionListener);
        timer.start();
    }
    
    protected abstract void move();   

    public abstract void moveTimerActionPerformed(ActionEvent e); 
    
    public void reset(){
        cell.removeMovingElement(this);
        cell = startCell;
        cell.addMovingElement(this);
    }
    
    protected Cell getStartCell(){
        return startCell;
    }
    
    public void setSpeed(int speed)
    {
        timer.setDelay(speed);
    }
    
    public void startTimer(){
        timer.start();
    }
    
    public void stopTimer(){
        timer.stop();
    }  
}
