package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Gate {
    enum GatePosition {
        OPEN,
        CLOSED,
        MOVING_OPEN,
        MOVING_CLOSED,
        MOVING_CYCLE,
        UNKNOWN
    }

    // Constants - Values copied from DECODE state finals
    public static final double CLOSE_POSITION = 1;
    public static final double OPEN_POSITION = 0.3;

    // Input buffer - Default wait time
    public static final long INPUT_BUFFER_WAIT_TIME = 100; // 100ms

    /**
     * Using a ReentrantLock (defaults to non-fair)
     */
    private final ReentrantLock gateLock = new ReentrantLock();
    private final Condition launcherReadyToFire = gateLock.newCondition();

    private Servo gate;

    private GatePosition gatePosition = GatePosition.UNKNOWN;

    public Gate(Servo gate) {
        this.gate = gate;
    }

    /**
     * Asynchronous, no feedback right now
     */
    public boolean openGate()
    {
        final boolean actionAttempted;
        if (gateLock.isLocked())
        {
            actionAttempted = false;
        }
        else
        {
            actionAttempted = true;

            new Thread(() -> {
                gateLock.lock();
                try
                {
                    gate.setPosition(OPEN_POSITION);
                    gatePosition = GatePosition.OPEN;
                }
                finally
                {
                    gateLock.unlock();
                }
            }).start();
        }

        return actionAttempted;
    }

    /**
     * Asynchronous, no feedback right now
     */
    public boolean closeGate()
    {
        final boolean actionAttempted;
        if (gateLock.isLocked())
        {
            actionAttempted = false;
        }
        else
        {
            actionAttempted = true;

            new Thread(() -> {
                gateLock.lock();
                try
                {
                    gate.setPosition(CLOSE_POSITION);
                    gatePosition = GatePosition.CLOSED;
                }
                finally
                {
                    gateLock.unlock();
                }
            }).start();
        }

        return actionAttempted;
    }

    /**
     * Cycles the gate.
     * Important: Assumes the gate is closed
     *
     * May want to account for that assumption at a later point.
     * @return
     */
    public boolean cycleGate() {
        final boolean actionAttempted;
        if (gateLock.isLocked()) {
            actionAttempted = false;
        } else {
            actionAttempted = true;

            new Thread(() -> {
                gateLock.lock();
                try {
                    gatePosition = GatePosition.MOVING_CYCLE;

                    gate.setPosition(OPEN_POSITION);
                    try {
                        Thread.sleep(1000); // 1 second wait
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    gate.setPosition(CLOSE_POSITION);

                    gatePosition = GatePosition.CLOSED;
                } finally {
                    gateLock.unlock();
                }
            }).start();
        }
        return actionAttempted;
    }


    public GatePosition getGatePosition()
    {
        return gatePosition;
    }

    public String getGatePositionString()
    {
        return gatePosition.toString();
    }
}
