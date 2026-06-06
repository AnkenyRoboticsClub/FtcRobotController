package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Launcher {
    private DcMotor leftLauncher, rightLauncher;

    private double motorSpeed = 0;

    private double defaultIncrement = 0.001;

    public Launcher(DcMotor leftLauncher, DcMotor rightLauncher) {
        this.leftLauncher = leftLauncher;
        this.rightLauncher = rightLauncher;
    }

    public void updateDefaultIncrement(double newDefaultIncrement)
    {
        defaultIncrement = newDefaultIncrement;
    }

    private void updateMotorPower()
    {
        leftLauncher.setPower(motorSpeed);
        rightLauncher.setPower(motorSpeed);
    }

    public double getMotorPower()
    {
        return motorSpeed;
    }

    public double incrementSpeed()
    {
        motorSpeed += defaultIncrement;
        // Above is the same as motorSpeed = motorSpeed + defaultIncrement;
         if (motorSpeed > 1)
            motorSpeed = 1;

        updateMotorPower();

        return motorSpeed;
    }

    public double decrementSpeed()
    {
        motorSpeed -= defaultIncrement;

        if (motorSpeed < 0)
            motorSpeed = 0;

        updateMotorPower();

        return motorSpeed;
    }

    public void stopLauncher()
    {
        motorSpeed = 0;

        updateMotorPower();
    }



}
