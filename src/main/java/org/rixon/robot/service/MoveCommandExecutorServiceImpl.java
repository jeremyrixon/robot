package org.rixon.robot.service;

import org.rixon.robot.pojo.NonMissingRobotStatus;
import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rixon.robot.pojo.NonMissingRobotStatus.Direction;

@Service
public class MoveCommandExecutorServiceImpl extends CommandExecutorService {

    @Value("${table.size:5}")
    private int tableSize;

    public MoveCommandExecutorServiceImpl() {
        super("MOVE");
    }

    @Override
    public RobotStatus commandRobot(RobotStatus robotStatus, RobotCommand command, List<RobotStatus> responses) {
        if (robotStatus instanceof NonMissingRobotStatus nonMissingRobotStatus) {
            int x = nonMissingRobotStatus.getX();
            int y = nonMissingRobotStatus.getY();
            final Direction facing = nonMissingRobotStatus.getFacing();
            switch (facing) {
                case NORTH -> y++;
                case EAST -> x++;
                case SOUTH -> y--;
                case WEST -> x--;
            }
            return x < 0 || x >= tableSize || y < 0 || y >= tableSize ? nonMissingRobotStatus : new NonMissingRobotStatus(x, y, facing);
        } else {
            return robotStatus;
        }

    }
}
