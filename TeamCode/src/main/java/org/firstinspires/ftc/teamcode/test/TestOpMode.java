package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.decode.Gate;
import org.firstinspires.ftc.teamcode.decode.Launcher;
import org.firstinspires.ftc.teamcode.positioning.Direction;

import java.util.List;

@TeleOp(name="Initial Offseason", group="Grove")
public class TestOpMode extends OpMode {

    //Hardware

    private Limelight3A limelight;

    /**
     * Drive motors
     */
    private DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

    private Launcher launcher;

    /**
     * Hopper gate servo
     */
    private Gate gate;

    private double rotationTestSpeed = 0.25; // Default value (without PID control)


    private String lastAction = "<none>";

    /**
     * User-defined init method
     * <p>
     * This method will be called once, when the INIT button is pressed.
     */
    @Override
    public void init() {
        initMecanum();
        initLauncher();
        initGate();
        initLimeLight();
    }



    private void initLimeLight () {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);
    }

    private void initMecanum()
    {
        // Drive motors
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

    private void initLauncher()
    {
        // Launcher motors
        DcMotor leftLauncherMotor = hardwareMap.get(DcMotor.class, "launcher1");
        DcMotor rightLauncherMotor  = hardwareMap.get(DcMotor.class, "launcher2");

        // Launcher encoders
        leftLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        leftLauncherMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        launcher = new Launcher(leftLauncherMotor, rightLauncherMotor);
    }

    private void initGate()
    {
        // Gate servo
        Servo gateServo = hardwareMap.get(Servo.class, "gate");
        gate = new Gate(gateServo);

        // Gate always stops closed
        gate.closeGate();
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

    private void testCodeRotationValue()
    {
        telemetry.addData("Rotation Value", rotationTestSpeed);

        double incrementValue = 0.0001;

        if (gamepad1.x)
            rotationTestSpeed = rotationTestSpeed - incrementValue;
        else if (gamepad1.b)
            rotationTestSpeed = rotationTestSpeed + incrementValue;

        if (rotationTestSpeed < 0)
            rotationTestSpeed = 0;

        if (rotationTestSpeed > 1)
            rotationTestSpeed = 1;

    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop() {
        telemetry.addData("Grove Offseason Testing","Version 1");
        telemetry.addData("Last Action", lastAction);
        telemetry.addData("Runtime", getRuntime());


        LLResult result = limelight.getLatestResult();
        limelightTelemetry(result);

        performRobotActions();

        // TODO Convert into action
        boolean isRightBumperPressed = gamepad1.right_bumper;
        if (isRightBumperPressed)
        {
            telemetry.addData("Drive Mode", "Auto Aim");
            autoAimCode(result);
        }

        manualDrive();


        telemetry.update();
    }

    private void performRobotActions()
    {
        List<RobotAction> robotActions = RobotAction.getActions(gamepad1);

        for (RobotAction robotAction : robotActions)
        {
            switch(robotAction)
            {
                case OPEN_GATE:
                    gate.openGate();
                    lastAction = "Gate Open";
                    break;
                case CLOSE_GATE:
                    gate.closeGate();
                    lastAction = "Gate Close";
                    break;
                case CYCLE_GATE:
                    gate.cycleGate();
                    lastAction = "Cycle Gate";
                    break;
                case INCREASE_LAUNCHER_SPEED:
                    launcher.incrementSpeed();
                    lastAction = "Increase Launcher Speed";
                    break;
                case DECREASE_LAUNCHER_SPEED:
                    launcher.decrementSpeed();
                    lastAction = "Decrease Launcher Speed";
                    break;
                case STOP_LAUNCHER:
                    launcher.stopLauncher();
                    lastAction = "Stop Launcher";
                    break;
            }
        }

        double launcherPower = launcher.getMotorPower();
        String gatePositionString = gate.getGatePositionString();

        telemetry.addData("Launcher Power", launcherPower);
        telemetry.addData("Gate Position", gatePositionString);
    }

    private void manualDrive()
    {
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
        drive(forward, strafe, rotate);
    }

    /**
     * Auto-Aim code goes here.
     */
    private void autoAimCode(LLResult result)
    {
        double targetXOffset = result.getTx();

        Direction turnDirection = getRotationDirection(targetXOffset);

        // Calculate input values for auto-aim
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = calculateRotationForAutoAim(targetXOffset, turnDirection);

        drive(forward, strafe, rotate);
    }

    /**
     * Calculates rotation value for a given X offset.
     * // TODO Figure out which direciton is positive rotation
     * // TODO Figure out a good power level
     * // TODO (Future) Implement PID control
     * Example
     * If left rotation (and left is positive) return 0.75
     * If right rotation (and left is positive) return -0.75
     * @param targetXOffset
     * @param turnDirection
     * @return
     */
    private double calculateRotationForAutoAim(double targetXOffset, Direction turnDirection) {
        double powerLevel = rotationTestSpeed;

        // Unsure which direction is positive rotation.
        // TODO: Test and update
        // First Test assume left is poitive
        if (turnDirection == Direction.LEFT) {
            // DO nothing
        }
        else if (turnDirection == Direction.RIGHT) {
            powerLevel = powerLevel * -1;
        }
        else {
            // Neither left, nor right
            powerLevel = 0;
        }

        return powerLevel;
    }

    private Direction getRotationDirection(double targetXOffset)
    {
        Direction turnDirection;

        // Calculate direction
        if (targetXOffset < 0)
        {
            turnDirection = Direction.LEFT;
        }
        else if (targetXOffset > 0)
        {
            turnDirection = Direction.RIGHT;
        }
        else {
            turnDirection = Direction.NONE;
        }

        return turnDirection;
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

    private void limelightTelemetry(LLResult result) {

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
        else
        {
            telemetry.addData("Limelight", "No target");
        }

    }
}
