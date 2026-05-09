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
    STOP_LAUNCHER;

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

        return actionList;
    }

}
