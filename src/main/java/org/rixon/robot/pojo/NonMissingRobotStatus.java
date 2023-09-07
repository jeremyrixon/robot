package org.rixon.robot.pojo;

import java.util.Map;

import org.rixon.robot.pojo.NonMissingRobotStatus.Direction;
import static org.rixon.robot.pojo.NonMissingRobotStatus.Direction.*;

public class NonMissingRobotStatus extends RobotStatus {

    public static final Map<Direction, Direction> leftTurns = Map.of(
            NORTH, WEST,
            WEST, SOUTH,
            SOUTH, EAST,
            EAST, NORTH
    );
    public static final Map<Direction, Direction> rightTurns = Map.of(
            NORTH, EAST,
            EAST, SOUTH,
            SOUTH, WEST,
            WEST, NORTH
    );
    private final int x;
    private final int y;
    private final Direction facing;

    public NonMissingRobotStatus(int x, int y, Direction facing) {
        super(false);
        this.x = x;
        this.y = y;
        this.facing = facing;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getFacing() {
        return facing;
    }

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
