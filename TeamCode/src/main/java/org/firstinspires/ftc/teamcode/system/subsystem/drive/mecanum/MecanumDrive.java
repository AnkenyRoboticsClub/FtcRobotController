package org.firstinspires.ftc.teamcode.system.subsystem.drive.mecanum;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

public class MecanumDrive {

    private HardwareMap hardwareMap;

    public final DcMotor motorFrontRight;
    public final DcMotor motorFrontLeft;
    public final DcMotor motorBackRight;
    public final DcMotor motorBackLeft;

    public MecanumDrive(HardwareMap hardwareMap,
                        MecanumMotorConfig frontRight, MecanumMotorConfig frontLeft,
                        MecanumMotorConfig backRight, MecanumMotorConfig backLeft)
    {
        motorFrontRight = hardwareMap.get(DcMotor.class, frontRight.getHardwareDeviceName());
        motorFrontRight.setDirection(frontRight.getDirection());
        motorFrontRight.setMode(frontRight.getRunMode());

        motorFrontLeft = hardwareMap.get(DcMotor.class, frontLeft.getHardwareDeviceName());
        motorFrontLeft.setDirection(frontLeft.getDirection());
        motorFrontLeft.setMode(frontLeft.getRunMode());

        motorBackRight = hardwareMap.get(DcMotor.class, backRight.getHardwareDeviceName());
        motorBackRight.setDirection(backRight.getDirection());
        motorBackRight.setMode(backRight.getRunMode());

        motorBackLeft = hardwareMap.get(DcMotor.class, backLeft.getHardwareDeviceName());
        motorBackLeft.setDirection(backLeft.getDirection());
        motorBackLeft.setMode(backLeft.getRunMode());
    }

    public DcMotor getFrontRightMotor() {
        return motorFrontRight;
    }

    public DcMotor getFrontLeftMotor() {
        return motorFrontLeft;
    }

    public DcMotor getBackRightMotor() {
        return motorBackRight;
    }

    public DcMotor getBackLeftMotor() { return motorBackLeft; }


    // Left Stick Forward, Backward, Strafe
    // Right Stick Turn
    public void drive(Gamepad gamepad) {
        double forward = gamepad.left_stick_y;
        double strafe = gamepad.left_stick_x;
        double rotate = gamepad.right_stick_x;

        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;
        double backLeftPower = forward - strafe + rotate;

        // Max is 1
        frontLeftPower = frontLeftPower > 1 ? 1 : frontLeftPower;
        frontRightPower = frontRightPower > 1 ? 1 : frontRightPower;
        backLeftPower = backLeftPower > 1 ? 1 : backLeftPower;
        backRightPower = backRightPower > 1 ? 1 : backRightPower;

        // Mix is -1
        frontLeftPower = frontLeftPower < -1 ? -1 : frontLeftPower;
        frontRightPower = frontRightPower > -1 ? -1 : frontRightPower;
        backLeftPower = backLeftPower > -1 ? -1 : backLeftPower;
        backRightPower = backRightPower > -1 ? -1 : backRightPower;

        motorFrontLeft.setPower(frontLeftPower);
        motorFrontRight.setPower(frontRightPower);
        motorBackLeft.setPower(backLeftPower);
        motorBackRight.setPower(backRightPower);
    }

}
