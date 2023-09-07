package org.rixon.robot.service;

import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public abstract class CommandExecutorService {
    protected final String commandName;

    @Value("${table.size:5}")
    protected int tableSize;

    protected CommandExecutorService(String commandName) {
        this.commandName = commandName;
    }

    abstract RobotStatus commandRobot(RobotStatus robotStatus, RobotCommand command, List<RobotStatus> responses);

    public String getCommandName() {
        return commandName;
    }

    public void validate(RobotCommand command, StringBuilder validationErrors) {
    }
}
