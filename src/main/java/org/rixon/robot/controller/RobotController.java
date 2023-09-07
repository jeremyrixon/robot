package org.rixon.robot.controller;

import org.rixon.robot.pojo.RobotCommand;
import org.rixon.robot.pojo.RobotStatus;
import org.rixon.robot.service.RobotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RobotController {

    final RobotService robotService;

    public RobotController(RobotService robotService) {
        this.robotService = robotService;
    }

    @PostMapping("/robotapi/")
    public List<RobotStatus> commandRobot(@RequestBody List<RobotCommand> commands) {
        robotService.validate(commands); // Will throw ResponseStatusException if validation fails
        return robotService.commandRobot(commands);
    }

}
