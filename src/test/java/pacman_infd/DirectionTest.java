package pacman_infd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacman_infd.Enums.Direction;

public class DirectionTest {

    @Test
    public void directionTests(){
        // Get opposite Tests
        assert (Direction.UP.getOpposite() == Direction.DOWN);
        assert (Direction.DOWN.getOpposite() == Direction.UP);
        assert (Direction.LEFT.getOpposite() == Direction.RIGHT);
        assert (Direction.RIGHT.getOpposite() == Direction.LEFT);

        // Get next direct clockwise test
        assert (Direction.UP.nextDirectionClockwise() == Direction.RIGHT);
        assert (Direction.DOWN.nextDirectionClockwise() == Direction.LEFT);
        assert (Direction.LEFT.nextDirectionClockwise() == Direction.UP);
        assert (Direction.RIGHT.nextDirectionClockwise() == Direction.DOWN);

        // Get next direct counter clockwise test
        assert (Direction.UP.nextDirectionCounterClockwise() == Direction.LEFT);
        assert (Direction.DOWN.nextDirectionCounterClockwise() == Direction.RIGHT);
        assert (Direction.LEFT.nextDirectionCounterClockwise() == Direction.DOWN);
        assert (Direction.RIGHT.nextDirectionCounterClockwise() == Direction.UP);
    }
}
