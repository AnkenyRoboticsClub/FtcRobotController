package org.firstinspires.ftc.teamcode.monarch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.system.subsystem.drive.mecanum.MecanumDrive;
import org.firstinspires.ftc.teamcode.system.subsystem.drive.mecanum.MecanumMotorConfig;


@TeleOp(name="Monarch-PostSeason", group="PostSeason")
public class Monarch extends OpMode {// Declare OpMode members.static enum DriveMode {

    // Constants
    private static final boolean IS_REVERSE = true;
    private static final boolean IS_FORWARD = false;

    private static final boolean WITH_ENCODER = true;
    private static final boolean WITHOUT_ENCODER = false;

    private ElapsedTime runtime = new ElapsedTime();

    private MecanumDrive mecanumDrive;

    private void initMecanumDrive()
    {
        // TODO Confirm direction
        MecanumMotorConfig frontRight = new MecanumMotorConfig("frontRight", IS_FORWARD, WITH_ENCODER);
        MecanumMotorConfig frontLeft = new MecanumMotorConfig("frontLeft", IS_FORWARD, WITH_ENCODER);
        MecanumMotorConfig backRight = new MecanumMotorConfig("backRight", IS_FORWARD, WITH_ENCODER);
        MecanumMotorConfig backLeft = new MecanumMotorConfig("backLeft", IS_FORWARD, WITH_ENCODER);

        mecanumDrive = new MecanumDrive(hardwareMap, frontRight, frontLeft, backRight, backLeft);
    }


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        initMecanumDrive();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }



    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {

        mecanumDrive.drive(gamepad1);


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());

        telemetry.update();
    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
