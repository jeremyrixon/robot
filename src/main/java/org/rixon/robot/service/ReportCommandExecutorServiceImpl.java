package org.rixon.robot.service;

import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportCommandExecutorServiceImpl extends CommandExecutorService {

    protected ReportCommandExecutorServiceImpl() {
        super("REPORT");
    }

    @Override
    public RobotStatus commandRobot(RobotStatus robotStatus, RobotCommand command, List<RobotStatus> responses) {
        responses.add(robotStatus);
        return robotStatus;
    }
}
