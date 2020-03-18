/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Elements;

import pacman_infd.Games.Cell;
import pacman_infd.Games.SoundManager;
import pacman_infd.Listeners.ElementEventListener;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Objects;


/**
 *
 * @author ivanweller
 */
public abstract class GameElement {
    protected Cell cell;
    protected ElementEventListener elementEventListener;
    protected SoundManager soundManager;
    
    public GameElement(){ }
    
    public GameElement(Cell cell, ElementEventListener elementEventListener, SoundManager soundManager)
    {
        this.cell = cell;
        this.elementEventListener = elementEventListener;
        this.soundManager = soundManager;
        cell.setStaticElement(this);
    }
    
    protected Point getPosition(){
        return new Point(cell.getXpos() * cell.getSize(), cell.getYPos() * cell.getSize());
    }

    /**
     * Draw this GameElement. Must be implemented in all child classes.
     * @param g Graphic object
     */
    public abstract void draw(Graphics g);
    
    /**
     * returns the cell containing this GameElement.
     * @return cell
     */
    public Cell getCell()
    {
        return cell;
    }
    
    /**
     * Set the cell of this GameElement, effectively repositions it.
     * @param cell target cell
     */
    public void setCell(Cell cell)
    {
        this.cell = cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameElement that = (GameElement) o;
        return cell.equals(that.cell) &&
                soundManager.equals(that.soundManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, soundManager);
    }
}
