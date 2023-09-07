## Toy Robot

Getting started: This is a spring boot gradle app so your IDE should be able to run it without any problems.

URL: The app runs at http://localhost:8080

The app allows you to queue up a sequence of commands and then execute them. If there are multiple REPORT commands, there will be multiple responses in the result. This makes it much easier to test out the robot.

Note: There is only one robot. While the app is running, it will remember where the robot is. If you'd like to see the robot missing from the table, you'll need to execute a REPORT command as the first command.

-Jeremy Rixon
