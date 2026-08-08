package org.firstinspires.ftc.teamcode.system.subsystem.drive;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

public class MecanumDrive {

    public final String MOTOR_FRONT_RIGHT;
    public final String MOTOR_FRONT_LEFT;
    public final String MOTOR_BACK_RIGHT;
    public final String MOTOR_BACK_LEFT;

    public final Direction FRONT_RIGHT_DIRECTION;
    public final Direction FRONT_LEFT_DIRECTION;
    public final Direction BACK_RIGHT_DIRECTION;
    public final Direction BACK_LEFT_DIRECTION;

    public MecanumDrive(
            String motorFrontRight, String motorFrontLeft,
            String motorBackRight, String motorBackLeft,
            Direction frontRightDirection, Direction frontLeftDirection,
            Direction backRightDirection, Direction backLeftDirection)
    {
        MOTOR_FRONT_RIGHT = motorFrontRight;
        MOTOR_FRONT_LEFT = motorFrontLeft;
        MOTOR_BACK_LEFT = motorBackLeft;
        MOTOR_BACK_RIGHT = motorBackRight;

        FRONT_RIGHT_DIRECTION = frontRightDirection;
        FRONT_LEFT_DIRECTION = frontLeftDirection;
        BACK_RIGHT_DIRECTION = backRightDirection;
        BACK_LEFT_DIRECTION = backLeftDirection;

    }

    public String getFrontRightMotorName() {
        return MOTOR_FRONT_RIGHT;
    }

    public String getFrontLeftMotorName() {
        return MOTOR_FRONT_LEFT;
    }

    public String getBackRightMotorName() {
        return MOTOR_BACK_RIGHT;
    }

    public String getBackLeftMotorName() {
        return MOTOR_BACK_LEFT;
    }

    public Direction getFrontRightDirection() {
        return FRONT_RIGHT_DIRECTION;
    }

    public Direction getFrontLeftDirection() {
        return FRONT_LEFT_DIRECTION;
    }

    public Direction getBackRightDirection() {
        return BACK_RIGHT_DIRECTION;
    }

    public Direction getBackLeftDirection() {
        return BACK_LEFT_DIRECTION;
    }

}
