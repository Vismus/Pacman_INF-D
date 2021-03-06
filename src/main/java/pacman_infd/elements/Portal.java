/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.elements;

import java.awt.Color;
import java.awt.Graphics;
import pacman_infd.games.Cell;
import pacman_infd.enums.Direction;
import pacman_infd.enums.PortalType;

/**
 *
 * @author Marinus
 */
public class Portal extends GameElement {
    private Portal linkedPortal;
    private PortalType type;

    public Portal(Cell cell, PortalType type) {
        super(cell, null);
        this.type = type;
    }

    public void remove() {
        if (cell.getStaticElement().equals(this)) {
            cell.setStaticElement(null);
        }
    }

    public void warpNeighbors() {
        setNeighborCell(Direction.UP);
        setNeighborCell(Direction.DOWN);
        setNeighborCell(Direction.LEFT);
        setNeighborCell(Direction.RIGHT);
    }

    private void setNeighborCell(Direction d) {
        if (cell.getNeighbor(d) != null && !cell.getNeighbor(d).hasWall()) {
            cell.getNeighbor(d).setNeighbor(d.getOpposite(), linkedPortal.getCell());
        }
    }

    public void setLinkedPortal(Portal portal) {
        linkedPortal = portal;
    }

    public Portal getLinkedPortal() {
        return linkedPortal;
    }

    @Override
    public void draw(Graphics g) {
        int n = linkedPortal == null ? 40 : 12;

        for (int i = 0; i < n; i++) {
            if (type.equals(PortalType.BLUE)) {
                g.setColor(new Color(i, 4 * i + 50, 255));
            } else {
                g.setColor(new Color(255, 4 * i + 50, i));
            }

            g.drawOval(
                    (int) getPosition().getX() + (getCell().getSize() / 2) - 20 + (i / 2),
                    (int) getPosition().getY() + (getCell().getSize() / 2) - 20 + (i / 2),
                    40 - i,
                    48 - i
            );
        }
    }
}
