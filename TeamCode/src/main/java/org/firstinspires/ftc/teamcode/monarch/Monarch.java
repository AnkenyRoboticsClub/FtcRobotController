package org.firstinspires.ftc.teamcode.monarch;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.system.drive.MecanumDrive;

public class Monarch {
    public static final String MOTOR_FRONT_RIGHT = "";
    public static final String MOTOR_FRONT_LEFT = "";
    public static final String MOTOR_BACK_RIGHT = "";
    public static final String MOTOR_BACK_LEFT = "";

    public final DcMotorSimple.Direction FRONT_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public final DcMotorSimple.Direction FRONT_LEFT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public final DcMotorSimple.Direction BACK_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public final DcMotorSimple.Direction BACK_LEFT_DIRECTION = DcMotorSimple.Direction.FORWARD;

    // Op Mode
    private HardwareMap hardwareMap;

    private MecanumDrive mecanumDrive;

    // Robot constants
    public final double MASS;

    // Pedro Pathing
    private MecanumConstants mecanumConstants;
    private FollowerConstants followerConstants;
    private Follower follower;

    private PathConstraints pathConstraints;


    public Monarch(HardwareMap hardwareMap, double mass) {
        this.hardwareMap = hardwareMap;
        this.MASS = mass;

        mecanumConstants = Constants.createMecanumConstants()
        pathConstraints = getPathConstraints();
        followerConstants = Constants.createFollowerConstraints(MASS);
        follower = createFollower(this.hardwareMap, pathConstraints);

    }

    private MecanumDrive createMecanumDrive()
    {
        this.mecanumDrive = new MecanumDrive(
                MOTOR_FRONT_RIGHT, MOTOR_FRONT_LEFT,
                MOTOR_BACK_RIGHT, MOTOR_BACK_LEFT,
                D)
    }


    private PathConstraints getPathConstraints() {
        PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
        return pathConstraints;
    }

    private Follower createFollower(HardwareMap hardwareMap, PathConstraints pathConstraints) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }
    private FollowerConstants getFollowerConstants () {
        FollowerConstants followerConstants = new FollowerConstants()
                .mass(5); // TODO: This is a test value - need to weight the robot (NOTE: the unit is kilograms)

        return followerConstants;
    }
}
