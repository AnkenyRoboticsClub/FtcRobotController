package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.system.subsystem.drive.MecanumDrive;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static FollowerConstants createFollowerConstraints(double mass)
    {
        FollowerConstants followerConstants = new FollowerConstants()
                .mass(mass);

        return followerConstants;
    }

    public static MecanumConstants createMecanumConstants(MecanumDrive mecanumDrive, double maxPower)
    {
        MecanumConstants driveConstants = new MecanumConstants()
                .maxPower(maxPower)
                .rightFrontMotorName(mecanumDrive.getFrontRightMotorName())
                .rightRearMotorName(mecanumDrive.getBackRightMotorName())
                .leftRearMotorName(mecanumDrive.getBackLeftMotorName())
                .leftFrontMotorName(mecanumDrive.getFrontLeftMotorName())
                .leftFrontMotorDirection(mecanumDrive.getFrontLeftDirection())
                .leftRearMotorDirection(mecanumDrive.getBackLeftDirection())
                .rightFrontMotorDirection(mecanumDrive.getFrontRightDirection())
                .rightRearMotorDirection(mecanumDrive.getBackRightDirection());

        return driveConstants;
    }

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }

}
