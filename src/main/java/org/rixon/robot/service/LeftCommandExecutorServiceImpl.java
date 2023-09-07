package org.rixon.robot.service;

import org.rixon.robot.pojo.NonMissingRobotStatus;
import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rixon.robot.pojo.NonMissingRobotStatus.Direction;

@Service
public class LeftCommandExecutorServiceImpl extends CommandExecutorService {

    public LeftCommandExecutorServiceImpl() {
        super("LEFT");
    }

    @Override
    public RobotStatus commandRobot(RobotStatus robotStatus, RobotCommand command, List<RobotStatus> responses) {
        if (robotStatus instanceof NonMissingRobotStatus nonMissingRobotStatus) {
            Direction newDirection = NonMissingRobotStatus.leftTurns.get(nonMissingRobotStatus.getFacing());
            return new NonMissingRobotStatus(nonMissingRobotStatus.getX(), nonMissingRobotStatus.getY(), newDirection);
        } else {
            return robotStatus;
        }
    }
}
