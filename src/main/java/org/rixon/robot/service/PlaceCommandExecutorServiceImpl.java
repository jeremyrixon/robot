package org.rixon.robot.service;

import org.rixon.robot.pojo.NonMissingRobotStatus;
import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rixon.robot.pojo.NonMissingRobotStatus.Direction;

@Service
public class PlaceCommandExecutorServiceImpl extends CommandExecutorService {

    public PlaceCommandExecutorServiceImpl() {
        super("PLACE");
    }

    @Override
    public void validate(RobotCommand command, StringBuilder validationErrors) {
        try {
            Direction.valueOf(command.getFacing());
        } catch (IllegalArgumentException ex) {
            validationErrors.append(String.format("Unrecognised direction: %s%n", command.getFacing()));
        }
    }

    @Override
    public RobotStatus commandRobot(RobotStatus robotStatus, RobotCommand command, List<RobotStatus> responses) {
        final int x = command.getX();
        final int y = command.getY();
        final Direction facing = Direction.valueOf(command.getFacing());
        final boolean isMissing = x < 0 || x >= tableSize || y < 0 || y >= tableSize;

        return isMissing ? new RobotStatus(true) : new NonMissingRobotStatus(x, y, facing);
    }
}
