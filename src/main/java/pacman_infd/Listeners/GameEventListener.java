/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Listeners;

/**
 *
 * @author Marinus
 */
public interface GameEventListener {
    public void decreaseLife();
    public void increasePoints(int amount);
    public void refocus();
}