/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.games;

import pacman_infd.enums.Direction;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import pacman_infd.elements.*;
import pacman_infd.enums.ElementType;
import pacman_infd.enums.PortalType;
import pacman_infd.listeners.EventHandler;
import pacman_infd.strategies.ChasePacmanStrategy;
import pacman_infd.strategies.MoveRandomStrategy;
import pacman_infd.strategies.Strategy;
import pacman_infd.utils.SoundManager;

import static pacman_infd.enums.ElementType.*;

/**
 *
 * @author Marinus
 */
public class GameWorld {
    private static final int CELL_SIZE = 26; //pixels

    private int width;
    private int height;

    private View view;
    private EventHandler eventHandler;

    private ArrayList<Cell> cells;
    private Cell[][] cellMap;

    private int gameSpeed;
    private int numberOfPelletsAtStart;

    private Portal portalBlue;
    private Portal portalOrange;

    public GameWorld(GameController gameController, char[][] levelMap, int speed) {
        view = gameController.getView();
        gameSpeed = speed;

        eventHandler = new EventHandler(gameController, this);

        if (levelMap != null) {

            width = levelMap[0].length;
            height = levelMap.length;
            createCells();
            findNeighbors();

            placeElements(levelMap, cellMap);

            numberOfPelletsAtStart = countPellets();
        }
    }

    /**
     * Create a grid of cells.
     */
    private void createCells() {
        cells = new ArrayList<>();
        cellMap = new Cell[height][width];

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Cell cell = new Cell(y, x, CELL_SIZE);
                cellMap[x][y] = cell;
                cells.add(cell);
            }
        }
    }

    /**
     * Finds all neighbors for each cell and adds them to the neighbors Map of
     * each cell.
     */
    private void findNeighbors() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                    cellMap[x][y].setNeighbor(Direction.UP, cellMap[(height + x - 1) % height][y]);
                    cellMap[x][y].setNeighbor(Direction.DOWN, cellMap[(height + x + 1) % height][y]);
                    cellMap[x][y].setNeighbor(Direction.LEFT, cellMap[x][(width + y - 1) % width]);
                    cellMap[x][y].setNeighbor(Direction.RIGHT, cellMap[x][(width + y + 1) % width]);
            }
        }
    }

    /**
     * Places walls on the cellMap according to wallMap
     *
     * @param elementMap array of integers representing the walls (1=wall, 0=no wall)
     * @param cellMap cell array of level.
     */
    private void placeElements(char[][] elementMap, Cell[][] cellMap) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                createElement(elementMap[x][y], cellMap[x][y]);
            }
        }
    }

    private void createElement(char element, Cell cellMap) {
        ElementType elementType = valueOfElement(element);
        switch (elementType) {
            case PELLET:
                new Pellet(cellMap, eventHandler);
                break;
            case SUPER_PELLET:
                new SuperPellet(cellMap, eventHandler);
                break;
            case BLINKY_GHOST:
            case PINKY_GHOST:
            case INKY_GHOST:
            case CLYDE_GHOST:
                createGhost(elementType, cellMap);
                break;
            case PACMAN:
                Pacman pacman = new Pacman(cellMap, eventHandler, gameSpeed);
                view.addKeyListener(pacman);
                break;
            case NO_ELEMENT:
                break;
            default:
                new Wall(cellMap, elementType);
                break;
        }
    }

    /**
     * Create a ghost to the cell of the map
     * @param element the type of ghost
     * @param cell the cell of the map
     */
    public void createGhost(ElementType element, Cell cell) {
        // By default, it's the BLINKY_GHOST, because we assume to know it's a ghost when we call this method
        Strategy strategy = new ChasePacmanStrategy();
        Color color = Color.RED;

        if (element == PINKY_GHOST) {
            strategy = new ChasePacmanStrategy();
            color = Color.PINK;
        } else if (element == INKY_GHOST) {
            strategy = new MoveRandomStrategy();
            color = Color.CYAN;
        } else if (element == CLYDE_GHOST) {
            strategy = new MoveRandomStrategy();
            color = Color.ORANGE;
        }

        new Ghost(cell, eventHandler, gameSpeed, strategy, color);
    }

    /**
     * Draw each cell in the game world.
     *
     * @param g Graphics object
     */
    private void drawCells(Graphics g) {
        if (cells != null) {
            for (Cell cell : cells) {
                cell.draw(g);
            }
        }
    }

    /**
     * Draw the game world.
     *
     * @param g is the instance of Graphics class
     */
    public void draw(Graphics g) {
        g.clearRect(0, 0, width * CELL_SIZE, height * CELL_SIZE);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width * CELL_SIZE, height * CELL_SIZE);
        drawCells(g);
    }

    /**
     * Counts the number of pellets currently in the GameWorld.
     *
     * @return number of pellets
     */
    public int countPellets() {
        int number = 0;
        for (Cell cell : cells) {
            if (cell.getStaticElement() instanceof Pellet) {
                number++;
            }
        }
        return number;
    }

    /**
     * Places a cherry on a random cell that has no static element.
     */
    public void placeCherryOnRandomEmptyCell() {
        if (countPellets() == numberOfPelletsAtStart / 2) {
            ArrayList<Cell> emptyCells = getEmptyCells();
            Random r = new Random();
            if (!emptyCells.isEmpty()) {
                new Cherry(emptyCells.get(r.nextInt(emptyCells.size() - 1)), eventHandler);
            }
        }
    }

    private void linkPortals() {
        portalBlue.setLinkedPortal(portalOrange);
        portalOrange.setLinkedPortal(portalBlue);
        portalBlue.warpNeighbors();
        portalOrange.warpNeighbors();
    }

    private void createPortal(Cell cell, PortalType portalType) {
        Portal portal = new Portal(cell, portalType);

        if (portalType == PortalType.BLUE) {
            if (getPortalBlue() != null) {
                getPortalBlue().remove();
            }
            setPortalBlue(portal);

            if (portalOrange != null) {
                linkPortals();
            }
        } else {
            if (getPortalOrange() != null) {
                getPortalOrange().remove();
            }
            setPortalOrange(portal);

            if (portalBlue != null) {
                linkPortals();
            }
        }
        SoundManager.playSound("portal");
    }

    public void spawnPortal(int x, int y, int mouseButton) {
        int cellX = x / CELL_SIZE;
        int cellY = y / CELL_SIZE;
        findNeighbors();
        if (cellY < cellMap.length) {
            if (!cellMap[cellY][cellX].hasWall() && cellMap[cellY][cellX].getStaticElement() == null) {
                if (mouseButton == 1) {
                    createPortal(getCell(cellX, cellY), PortalType.BLUE);
                } else if (mouseButton == 3) {
                    createPortal(getCell(cellX, cellY), PortalType.ORANGE);
                }
            }
        }
    }
    
    public void clearGameWorld() {
        for(Cell cell: cells) {
            cell.clearCell();
        }
        eventHandler = null;
        cellMap = null;
        cells = null;
    }

    /**
     * Returns a list of all cells that have no static element placed on them.
     *
     * @return list of cells
     */
    private ArrayList<Cell> getEmptyCells() {
        ArrayList<Cell> emptyCells = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.getStaticElement() == null) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    /**
     *
     * @return number of Pellets at the start of the game.
     */
    public int getNumberOfPelletsAtStart() {
        return numberOfPelletsAtStart;
    }

    /**
     * @return the portalBlue
     */
    public Portal getPortalBlue() {
        return portalBlue;
    }

    /**
     * @param portalBlue the portalBlue to set
     */
    public void setPortalBlue(Portal portalBlue) {
        this.portalBlue = portalBlue;
    }

    /**
     * @return the portalOrange
     */
    public Portal getPortalOrange() {
        return portalOrange;
    }

    /**
     * @param portalOrange the portalOrange to set
     */
    public void setPortalOrange(Portal portalOrange) {
        this.portalOrange = portalOrange;
    }

    public Cell getCell(int x, int y) {
        if (y >= 0 && y < cellMap.length) {
            if (x >= 0 && x < cellMap[y].length) {
                return cellMap[y][x];
            }
        }

        return null;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }
}
