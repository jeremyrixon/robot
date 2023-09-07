package org.rixon.robot.pojo;

public class RobotStatus {
    private final boolean isMissing;

    public RobotStatus(boolean isMissing) {
        this.isMissing = isMissing;
    }

    public RobotStatus() {
        this.isMissing = true;
    }

    public boolean isMissing() {
        return isMissing;
    }
}
