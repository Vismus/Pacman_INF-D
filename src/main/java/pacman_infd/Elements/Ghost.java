/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import pacman_infd.Games.Cell;
import pacman_infd.Enums.GhostState;
import pacman_infd.Games.SoundManager;
import pacman_infd.Listeners.ElementEventListener;
import pacman_infd.Strategies.FleeStrategy;
import pacman_infd.Strategies.ReturnHomeSrategy;
import pacman_infd.Strategies.Strategy;

/**
 *
 * @author Marinus
 */
public class Ghost extends MovingGameElement implements Eatable{
    private Strategy strategy;
    private Strategy initialStrategy;
    private Color color;
    private GhostState state;

    private Timer stateSoonChangeTimer; // To say when the state ghost change from VULNERABLE to NORMAL
    private Timer vulnerabilityTimer;
    private Timer deathTimer;
    private static final int VULNERABLE_TIMER_DELAY = 8000; // plus quarter than total (for the rendering blue and white when ghost will changed state (VULNERABLE -> NORMAL)
    private static final int DEATH_TIMER_DELAY = 15000;

    private static final int VALUE = 400;

    public Ghost(Cell cell, ElementEventListener gameEventListener, int speed, Strategy strategy, Color color, SoundManager sMger) {
        super(cell, gameEventListener, speed, sMger);
        this.strategy = strategy;
        this.color = color;
        initialStrategy = strategy;
        state = GhostState.NORMAL;

        ActionListener vulnerabilityTimerAction = new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vulnerabilityTimerActionPerformed(evt);
            }
        };

        ActionListener deathTimerAction = new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deathTimerActionPerformed(evt);
            }
        };

        ActionListener changeStateAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changeStateActionPerformed(actionEvent);
            }
        };

        stateSoonChangeTimer = new Timer(VULNERABLE_TIMER_DELAY / 4, changeStateAction);
        vulnerabilityTimer = new Timer(VULNERABLE_TIMER_DELAY, vulnerabilityTimerAction);
        deathTimer = new Timer(DEATH_TIMER_DELAY, deathTimerAction);
    }

    /**
     * Draw this Ghost
     *
     * @param g
     */
    public void draw(Graphics g) {
        if (state.equals(GhostState.VULNERABLE)) {
            if (stateSoonChangeTimer.isRunning() && g.getColor().equals(Color.BLUE)) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLUE);
            }
        } else if (state.equals(GhostState.DEAD)) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(color);
        }
        //body
        g.fillRoundRect(
                (int) getPosition().getX() - 5,
                (int) getPosition().getY() - 5,
                getCell().getSize() + 10,
                getCell().getSize() + 10,
                10, 5
        );
        // eyes
        g.setColor(Color.WHITE);
        // left eye
        g.fillOval(
                (int) getPosition().getX(),
                (int) getPosition().getY(),
                12,
                12
        );
        // right eye
        g.fillOval(
                (int) getPosition().getX() + 20,
                (int) getPosition().getY(),
                12,
                12
        );
        // pupils
        g.setColor(Color.BLACK);
        // left pupil
        g.fillOval(
                (int) getPosition().getX() + 4,
                (int) getPosition().getY() + 2,
                6,
                6
        );
        // right pupil
        g.fillOval(
                (int) getPosition().getX() + 24,
                (int) getPosition().getY() + 2,
                6,
                6
        );
    }

    /**
     * Move to the next cell. Uses its current strategy to determine which cell
     * to move to.
     */
    protected void move() {
        Cell nextCell = strategy.giveNextCell(cell);
        if (nextCell != null) {
            nextCell.addMovingElement(this);
            cell.removeMovingElement(this);
            setCell(nextCell);
        }

    }

    /**
     * Change current strategy to FleeStrategy and lowers the speed of this
     * Ghost by 50% This is called when Pacman eats a superPellet.
     */
    public void flee() {
        if (state.equals(GhostState.VULNERABLE)) {
            vulnerabilityTimer.restart();
            stateSoonChangeTimer.stop();
        } else if (state.equals(GhostState.NORMAL)) {
            this.strategy = new FleeStrategy();
            state = GhostState.VULNERABLE;
            setSpeed((int) (speed * 1.50));
            vulnerabilityTimer.start();
        }

    }

    /**
     * Revert back to the initial Strategy and initial speed and stops.
     */
    public void backToNormal() {
        strategy = initialStrategy;
        state = GhostState.NORMAL;
        setSpeed(speed);
        vulnerabilityTimer.stop();
        deathTimer.stop();
    }

    public void dead() {
        setSpeed(speed);
        strategy = new ReturnHomeSrategy(startCell);
        state = GhostState.DEAD;
        deathTimer.start();
    }
    
    @Override
    public void reset(){
        super.reset();
        backToNormal();
    }
    
    public GhostState getState(){
        return state;
    }

    /**
     * This is called each 'tick' of the timer. This is used by the GameWorld to
     *
     * @param e
     */
    @Override
    public void moveTimerActionPerformed(ActionEvent e) {
        move();
        //checkCollisions();
        elementEventListener.movingElementActionPerformed(this);
    }

    public void changeStateActionPerformed(ActionEvent e) {
        if (state.equals(GhostState.VULNERABLE)) {
            backToNormal();
        } else if (state.equals(GhostState.DEAD)) {
            deathTimer.restart();
        }
    }

    private void vulnerabilityTimerActionPerformed(ActionEvent evt) {
        if (state.equals(GhostState.VULNERABLE)) {
            stateSoonChangeTimer.start();
        }
        vulnerabilityTimer.stop();
    }

    private void deathTimerActionPerformed(ActionEvent evt) {
        backToNormal();
        deathTimer.stop();
    }
    
    @Override
    public void eatMe() {
        if (state.equals(GhostState.VULNERABLE)) {
            soundManager.playSound("ghost");
            dead();
        }
        else if (state.equals(GhostState.NORMAL)) {
            soundManager.playSound("death");
            elementEventListener.killPacman();
        }
    }

    @Override
    public int getValue() {
        return VALUE;
    }
}
