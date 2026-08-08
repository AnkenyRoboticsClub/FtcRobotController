package org.firstinspires.ftc.teamcode.offseason;

import static org.firstinspires.ftc.teamcode.decode.DriveMode.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.decode.DriveMode;
import org.firstinspires.ftc.teamcode.test.RobotAction;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;


@TeleOp(name="Bumblebee", group="Offseason")
public class Bumblebee extends OpMode {// Declare OpMode members.static enum DriveMode {
    private static final DriveMode[] DRIVE_MODES = {DRIVE_MODE_A, DRIVE_MODE_B, DRIVE_MODE_C, DRIVE_MODE_D};
    private static final int NUMBER_OF_DRIVE_MODES = DRIVE_MODES.length;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private int driveMode = 0; // Default to index 0


    private DcMotor intake = null;

    private long nextDriveModeUpdate = Calendar.getInstance().getTimeInMillis();

    private double drivePower = 1.0;
    private long nextDrivePowerUpdate = Calendar.getInstance().getTimeInMillis();

    boolean isTurboEngaged = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive = hardwareMap.get(DcMotor.class, "motorLeft");
        rightDrive = hardwareMap.get(DcMotor.class, "motorRight");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Intake
        intake = hardwareMap.get(DcMotor.class, "intake");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;

        isTurboEngaged = false;

        List<RobotAction> robotActions = RobotAction.getActions(gamepad1, gamepad2);
        for (RobotAction robotAction : robotActions)
        {
            if (robotAction == RobotAction.SWITCH_CONTROL_SCHEME)
            {
                // First check to see if enough time has passed since the last button press
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime >= nextDriveModeUpdate)
                {
                    driveMode++;
                    if (driveMode == NUMBER_OF_DRIVE_MODES)
                        driveMode = 0;

                    nextDriveModeUpdate = currentTime + 500;
                }
            }

            if (robotAction == RobotAction.SPEED_UP)
            {
                // First check to see if enough time has passed since the last button press
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime >= nextDrivePowerUpdate)
                {
                    drivePower = drivePower + 0.1;
                    if (drivePower > 1)
                        drivePower = 1;

                    nextDrivePowerUpdate = currentTime + 500;
                }
            }

            if (robotAction == RobotAction.SPEED_DOWN)
            {
                // First check to see if enough time has passed since the last button press
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime >= nextDrivePowerUpdate)
                {
                    drivePower = drivePower - 0.1;
                    if (drivePower < 0.1)
                        drivePower = 0.1;

                    nextDrivePowerUpdate = currentTime + 500;
                }
            }

            if (robotAction == RobotAction.TURBO)
                isTurboEngaged = true;

        }

        double intakeIn = (double)gamepad1.right_trigger;
        double intakeOut = (double)gamepad1.left_trigger;

        double intakePower = 0;
        if (intakeIn > 0 && intakeOut > 0)
            intakePower = 0; // Do nothing
        else if (intakeIn > 0)
            intakePower = intakeIn;
        else if (intakeOut > 0)
            intakePower = intakeOut * -1;

        intake.setPower(intakePower);
        telemetry.addData("Intake", intakePower);


        DriveMode controlScheme = DRIVE_MODES[driveMode];
        drive(controlScheme);


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());

        telemetry.update();
    }

    private void drive(DriveMode driveMode)
    {
        telemetry.addData("Drive Power", drivePower);

        double drive = 0;
        double turn = 0;

        switch(driveMode)
        {
            case DRIVE_MODE_A:
                drive = -gamepad1.right_stick_y;
                turn = gamepad1.left_stick_x;
                break;
            case DRIVE_MODE_B:
                drive = -gamepad1.right_stick_y;
                turn = gamepad1.right_stick_x;
                break;
            case DRIVE_MODE_C:
                drive = -gamepad1.left_stick_y;
                turn = gamepad1.right_stick_x;
                break;
            case DRIVE_MODE_D:
                drive = -gamepad1.left_stick_y;
                turn = gamepad1.left_stick_x;
                break;
        }
        // Update telemetry to print drive mode
        telemetry.addData("Drive Mode", driveMode);


        double leftPower = 0;
        double rightPower = 0;
        leftPower = Range.clip(drive + turn, -1.0, 1.0);
        rightPower = Range.clip(drive - turn, -1.0, 1.0);

        // Adjust drive power;
        double powerAdjust = drivePower;

        if (isTurboEngaged)
            powerAdjust = 1;

        leftPower = leftPower * powerAdjust;
        rightPower = rightPower * powerAdjust;

        // Send calculated power to wheels
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);

        telemetry.addData("Is Turbo Engaged", isTurboEngaged);
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
