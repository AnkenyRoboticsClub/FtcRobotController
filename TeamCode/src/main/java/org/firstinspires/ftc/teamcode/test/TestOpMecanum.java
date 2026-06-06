package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp(name="Initial Setup Mecanum", group="Grove")
public class TestOpMecanum extends OpMode {
    // Hardware
    // Hardware - Motors
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    // Hardware - Limelight
    private Limelight3A limelight;

    // Hardware - Odometry
    private GoBildaPinpointDriver pinpoint;

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
        initGoBuilda4BarOdometry();
    }

    private void initGoBuilda4BarOdometry()
    {
        // Get a reference to the sensor
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // Configure the sensor
        configurePinpoint();

        // Default from "SensorGoBuildaPinpoint.java"
        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }

    /**
     * Unchanged from exmaple
     * @see org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint.configurePinpoint()
     */
    public void configurePinpoint(){
        /*
         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
         *
         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
         *  Left of the center is a positive number, right of center is a negative number.
         *
         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
         *  Forward of center is a positive number, backwards is a negative number.
         */
        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();
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
        telemetry.addData("Drive Mode", "Mecanum");
        telemetry.addData("Runtime", getRuntime());

        // Output Limelight data
        limelightTelemetry();

        // The controller's left/right stick returns a value from 0-1
        double leftStickForwardAndBackward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotation = gamepad1.right_stick_x;

        telemetry.addData("Left Stick y axis", gamepad1.left_stick_y);
        drive(leftStickForwardAndBackward, strafe, rotation);

        // Variable types
        // Double = decimal point number ("double" point precision)
        double testDouble = 0.1234;

        // int = Whole number (integer)
        int wholeNumber = 1;

        // String = text
        String sentence = "This is a sentence";
        String oneCharacter = "c";
        String wholeParagraph = "First line\nSecond Line";



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
