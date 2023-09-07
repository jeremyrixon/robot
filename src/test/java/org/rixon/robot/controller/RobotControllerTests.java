package org.rixon.robot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RobotControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public static class Command {
        private final String command;

        public Command(final String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }
    public static class PlaceCommand extends Command {
        private final int x;
        private final int y;
        private final String facing;

        public PlaceCommand(int x, int y, String facing) {
            super("PLACE");
            this.x = x;
            this.y = y;
            this.facing = facing;
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
    private final Command left = new Command("LEFT");
    private final Command right = new Command("RIGHT");
    private final Command move = new Command("MOVE");
    private final Command report = new Command("REPORT");
    private final Command invalid = new Command("INVALID");

    @Test
    public void shouldReportPlacedPosition() throws Exception {

        final int x = 1;
        final int y = 2;
        final String facing = "EAST";
        List<Command> commands = List.of(
                new PlaceCommand(x, y, facing),
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].missing").value(false),
                        jsonPath("$[0].x").value(x),
                        jsonPath("$[0].y").value(y),
                        jsonPath("$[0].facing").value(facing)
                );
    }

    @Test
    public void shouldReportMissingWhenPlacedInInvalidPosition() throws Exception {

        final int x = -1;
        final int y = -1;
        List<Command> commands = List.of(
                new PlaceCommand(x, y, "EAST"),
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].missing").value(true)
                );
    }

    @Test
    public void shouldReturnA400IfCommandIsInvalid() throws Exception {

        List<Command> commands = List.of(
                invalid
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void shouldReturnA400IfDirectionIsInvalid() throws Exception {

        List<Command> commands = List.of(
                new PlaceCommand(0, 0, "INVALID")
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldTurnCorrectly() throws Exception {

        List<Command> commands = List.of(
                new PlaceCommand(0, 0, "NORTH"),
                right,
                report,
                right,
                report,
                right,
                report,
                right,
                report,
                left,
                report,
                left,
                report,
                left,
                report,
                left,
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].facing").value("EAST"),
                        jsonPath("$[1].facing").value("SOUTH"),
                        jsonPath("$[2].facing").value("WEST"),
                        jsonPath("$[3].facing").value("NORTH"),
                        jsonPath("$[4].facing").value("WEST"),
                        jsonPath("$[5].facing").value("SOUTH"),
                        jsonPath("$[6].facing").value("EAST"),
                        jsonPath("$[7].facing").value("NORTH")
                );
    }

    @Test
    public void shouldMoveCorrectly() throws Exception {

        List<Command> commands = List.of(
                new PlaceCommand(0, 0, "NORTH"),
                report,

                move,
                report,

                right,
                move,
                report,

                right,
                move,
                report,

                right,
                move,
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].x").value(0),
                        jsonPath("$[0].y").value(0),
                        jsonPath("$[1].x").value(0),
                        jsonPath("$[1].y").value(1),
                        jsonPath("$[2].x").value(1),
                        jsonPath("$[2].y").value(1),
                        jsonPath("$[3].x").value(1),
                        jsonPath("$[3].y").value(0),
                        jsonPath("$[4].x").value(0),
                        jsonPath("$[4].y").value(0)
                );
    }

    @Test
    public void shouldNotLeaveTable() throws Exception {

        List<Command> commands = List.of(
                new PlaceCommand(0, 0, "WEST"),
                move,
                report,

                left,
                move,
                report,

                new PlaceCommand(4, 4, "EAST"),
                move,
                report,

                left,
                move,
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].x").value(0),
                        jsonPath("$[0].y").value(0),
                        jsonPath("$[1].x").value(0),
                        jsonPath("$[1].y").value(0),
                        jsonPath("$[2].x").value(4),
                        jsonPath("$[2].y").value(4),
                        jsonPath("$[3].x").value(4),
                        jsonPath("$[3].y").value(4)
                );
    }

    @Test
    public void shouldRememberWhereItIs() throws Exception {

        List<Command> commands = List.of(
                new PlaceCommand(2, 3, "WEST"),
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].x").value(2),
                        jsonPath("$[0].y").value(3),
                        jsonPath("$[0].facing").value("WEST")
                );

        List<Command> commands2 = List.of(
                report
        );

        mockMvc.perform(
                        post("/robotapi/")
                                .content(objectMapper.writeValueAsString(commands2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].x").value(2),
                        jsonPath("$[0].y").value(3),
                        jsonPath("$[0].facing").value("WEST")
                );
    }

}
