package org.firstinspires.ftc.teamcode.system.subsystem.drive.mecanum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumMotorConfig
{
    private String hardwareDeviceName;
    private DcMotorSimple.Direction direction;
    private DcMotor.RunMode runMode;

    public MecanumMotorConfig(String hardwareDeviceName, boolean isReverse, boolean useEncoder)
    {
        this.hardwareDeviceName = hardwareDeviceName;

        if (isReverse)
            this.direction = DcMotorSimple.Direction.REVERSE;
        else
            this.direction = DcMotorSimple.Direction.FORWARD;

        if (useEncoder)
            this.runMode = DcMotor.RunMode.RUN_USING_ENCODER;
        else
            this.runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
    }

    public String getHardwareDeviceName() {
        return hardwareDeviceName;
    }

    public DcMotorSimple.Direction getDirection() {
        return direction;
    }

    public DcMotor.RunMode getRunMode() {
        return runMode;
    }
}