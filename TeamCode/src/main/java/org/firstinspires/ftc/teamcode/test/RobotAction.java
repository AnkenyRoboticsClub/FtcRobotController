package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.List;

public enum RobotAction {
    OPEN_GATE,
    CLOSE_GATE,
    CYCLE_GATE,
    INCREASE_LAUNCHER_SPEED,
    DECREASE_LAUNCHER_SPEED,
    STOP_LAUNCHER,
    SWITCH_CONTROL_SCHEME,
    SPEED_UP,
    SPEED_DOWN,
    TURBO,
    INTAKE_FORWARD,
    INTAKE_REVERSE;

    public static List<RobotAction> getActions(Gamepad gamepad)
    {
        List<RobotAction> actionList = new ArrayList<RobotAction>();

        if (gamepad.dpad_left)
            actionList.add(OPEN_GATE);

        if (gamepad.dpad_right)
            actionList.add(CLOSE_GATE);

        if (gamepad.dpad_up)
            actionList.add(CYCLE_GATE);

        if (gamepad.x)
            actionList.add(DECREASE_LAUNCHER_SPEED);

        if (gamepad.b)
            actionList.add(INCREASE_LAUNCHER_SPEED);

        if (gamepad.a)
            actionList.add(STOP_LAUNCHER);

        if (gamepad.left_bumper)
            actionList.add(SWITCH_CONTROL_SCHEME);

        return actionList;
    }

    public static List<RobotAction> getActions(Gamepad gamepad1, Gamepad gamepad2)
    {
        List<RobotAction> robotActions = getActions(gamepad1);

        if(gamepad2.left_bumper)
            robotActions.add(SPEED_DOWN);

        if(gamepad2.right_bumper)
            robotActions.add(SPEED_UP);

        if (gamepad2.right_trigger > 0.5)
            robotActions.add(TURBO);

        return robotActions;
    }

}
