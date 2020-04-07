package pacman_infd.elements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pacman_infd.enums.Direction;

public class PacmanTest extends GameElementTest {
    Pacman p;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        p = new Pacman(gameWorld.getCell(0,0), gameWorld.getEventHandler(), 0);
    }

    @Test
    public void testCreate() {
        // Check if pacman is correctly created into the cell (0,0)
        assert (p.getCell().getXpos() == 0 && p.getCell().getYPos() == 0);
        assert (p.getStartCell().equals(p.getCell()));
        assert (p.getCurrentDirection() == null);

        // Check that pacman doesn't move, because the current direction is not set
        p.move();
        gameWorld.getEventHandler().movingElementActionPerformed(p);
        assert (p.getStartCell().equals(p.getCell()));
    }

    /**
     * Check if pacman move to the correct direction when we set the direction
     */
    @Test
    public void testMove() {
        assert (p.getCell().getXpos() == 1 && p.getCell().getYPos() == 0);

        p.changeDirection(Direction.RIGHT);
        p.move();
        gameWorld.getEventHandler().movingElementActionPerformed(p);

        assert (p.getCell().getXpos() == 1 && p.getCell().getYPos() == 0);
    }

    /**
     * test if the changement of the direction of pacman is correctly manage
     */
    @Test
    public void testChangeDirection() {
        p.changeDirection(Direction.RIGHT);
        p.move();
        gameWorld.getEventHandler().movingElementActionPerformed(p);

        assert (p.getCurrentDirection().equals(Direction.RIGHT));
        assert (p.getCell().getXpos() == 1 && p.getCell().getYPos() == 0);

        // Try to go up, normally it is not possible because it is a wall
        // Then the current direction must always be RIGHT
        p.changeDirection(Direction.UP);
        assert (p.getCurrentDirection().equals(Direction.RIGHT));

        // Now, we check that pacman correctly change its current direction to DOWN
        p.changeDirection(Direction.DOWN);
        assert (p.getCurrentDirection().equals(Direction.DOWN));
    }
}