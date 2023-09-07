package org.rixon.robot.pojo;

public class RobotCommand {
    public enum Names {
        PLACE,
        MOVE,
        LEFT,
        RIGHT,
        REPORT
    }

    private final String command;
    private final int x;
    private final int y;
    private final String facing;

    public RobotCommand(final String commandName, int x, int y, final String facing) {
        this.command = commandName;
        this.x = x;
        this.y = y;
        this.facing = facing;
    }

    public String getCommand() {
        return command;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getFacing() {
        return facing;
    }
}
