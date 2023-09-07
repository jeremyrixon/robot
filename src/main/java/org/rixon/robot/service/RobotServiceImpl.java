package org.rixon.robot.service;

import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RobotServiceImpl implements RobotService {

    private RobotStatus currentStatus = new RobotStatus();
    private final Map<String, CommandExecutorService> commandExecutors;

    @Autowired
    public RobotServiceImpl(List<CommandExecutorService> commandExecutors) {
        this.commandExecutors = commandExecutors.stream().collect(
                Collectors.toMap(CommandExecutorService::getCommandName, Function.identity()));
    }

    @Override
    public List<RobotStatus> commandRobot(List<RobotCommand> commands) {
        List<RobotStatus> reports = new ArrayList<>();
        for (RobotCommand command : commands) {
            final CommandExecutorService commandExecutorService = commandExecutors.get(command.getCommand());
            this.currentStatus = commandExecutorService.commandRobot(currentStatus, command, reports);
        }
        return reports;
    }

    @Override
    public void validate(List<RobotCommand> commands) {
        StringBuilder validationErrors = new StringBuilder();
        for (RobotCommand command : commands) {
            CommandExecutorService commandExecutorService = commandExecutors.get(command.getCommand());
            if (commandExecutorService != null) {
                commandExecutorService.validate(command, validationErrors);
            } else {
                validationErrors.append(String.format("Unrecognized command: %s%n", command.getCommand()));
            }
        }
        if (!validationErrors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationErrors.toString());
        }
    }
}
