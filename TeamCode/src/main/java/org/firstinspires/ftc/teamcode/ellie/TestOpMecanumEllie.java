package org.firstinspires.ftc.teamcode.ellie;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp(name="Initial Setup Ellie Test", group="Ellie")
public class TestOpMecanumEllie extends OpMode {

    //Hardware

    private Limelight3A limelight;
        private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private static final double MAX_POWER = 1.0;

    /**
     * User-defined init method
     * <p>
     * This method will be called once, when the INIT button is pressed.
     */
    @Override
    public void init() {
        initMecanum();
        initLimeLight();
    }



    private void initLimeLight () {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);
    }

    private void initMecanum()
    {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "leftFront");
        frontRightDrive = hardwareMap.get(DcMotor.class, "rightFront");
        backLeftDrive = hardwareMap.get(DcMotor.class, "leftBack");
        backRightDrive = hardwareMap.get(DcMotor.class, "rightBack");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        // Ideally this would run with encoders
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /**
     * User-defined init_loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the init button is pressed and when the play button is pressed (or the
     * OpMode is stopped).
     * <p>
     * This method is optional. By default, this method takes no action.
     */
    @Override
    public void init_loop() {
        super.init_loop();
    }

    /**
     * User-defined start method
     * <p>
     * This method will be called once, when the play button is pressed.
     * <p>
     * This method is optional. By default, this method takes no action.
     * <p>
     * Example usage: Starting another thread.
     */
    @Override
    public void start() {
        super.start();
        limelight.start();
        resetRuntime();
    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop() {
        telemetry.addData("Ellie's Test","Limelight Setup");
        telemetry.addData("Drive Mode", "Mecanum");
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Left Stick y axis", gamepad1.left_stick_y);


        limelightTelemetry();


        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
        drive(forward, right, rotate);

        telemetry.update();
    }

    /**
     * User-defined stop method
     * <p>
     * This method will be called once, when this OpMode is stopped.
     * <p>
     * Your ability to control hardware from this method will be limited.
     * <p>
     * This method is optional. By default, this method takes no action.
     */
    @Override
    public void stop() {
        super.stop();
    }

    private void drive(double forward, double right, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }

    private void limelightTelemetry() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {

            // read the results
            //Pose3D robotPose = result.getBotpose_MT2();
            Pose3D robotPose = result.getBotpose();

            // turns the robot to face the tag
            //faceTag(Math.toRadians(result.getTx()));

                /*double xInches = robotPose.getPosition().x * 39.3701; // convert from LL meters to RR inches
                double yInches = robotPose.getPosition().y * 39.3701; // convert from LL meters to RR inches
                double headingRadians = Math.toRadians(robotPose.getOrientation().getYaw()); // convert from LL degrees to RR radians
                drive.localizer.setPose(new Pose2d(xInches, yInches, headingRadians)); // sets the RR pose to pose from LL*/

            //double distance = getDistanceFromTag(result.getTa());

            // print out data from results
            telemetry.addData("target x", result.getTx());
            telemetry.addData("target y", result.getTy());
            telemetry.addData("distance", result.getBotposeAvgDist());
            //telemetry.addData("target area", result.getTa());
            //telemetry.addData("distance", distance);
            telemetry.addData("robot yaw", robotPose.getOrientation().getYaw());
            telemetry.addData("robot x", robotPose.getPosition().x);
            telemetry.addData("robot y", robotPose.getPosition().y);
            telemetry.addData("robot z", robotPose.getPosition().z);

            //telemetry.addData("robot heading", robotPose.getOrientation().getYaw());
            //telemetry.addData("robot position", robotPose.getPosition());

        }

    }
}
