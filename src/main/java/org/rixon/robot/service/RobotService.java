package org.rixon.robot.service;

import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;

import java.util.List;

public interface RobotService {
    List<RobotStatus> commandRobot(List<RobotCommand> commands);

    void validate(List<RobotCommand> commands);
}
