package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Runs a DC motor to a given position in it's own thread other things can happen while the motor is running to that position
 */
public class EncoderThread implements Runnable {

    DcMotor motor;

    private double targetPosition;
    private double power;
    private double TICKS_PER_REV;
    private double radius;
    Thread t = new Thread(this);

    /**
     * @param motor this is the motor that the encoder is attached to
     * @param gearReduction this is is the gear reduction in that motor if a neverest 40 then this is 40
     */
    public EncoderThread(DcMotor motor, double gearReduction) {
        this.motor = motor;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TICKS_PER_REV = 7 * gearReduction;
    }

    /**
     * runs to a set position at a set speed with a position set by setTargetPosition and speed set by setPower
     *
     * or called by another method in this class
     */
    @Override
    public void run() {

        //record all the initial stuff
        long t = System.currentTimeMillis();
        double initialPos = motor.getCurrentPosition();
        DcMotor.RunMode mode = motor.getMode();

        //run to the position
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition((int) Math.round(targetPosition));
        motor.setPower(power);

        //wait to get to the position or timeout
        while (motor.isBusy() && System.currentTimeMillis() < t + Math.abs(targetPosition - initialPos) * 1 * power) {

        }

        //turn off and set the mode back to the original
        motor.setPower(0);
        motor.setMode(mode);

    }

    /**
     * sets the target position in ticks for the motor belonging to this object DOES NOT run to that position
     *
     * @param targetPosition target position in ticks
     */
    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * sets the power for this object DOES NOT run to that position
     *
     * @param power power to set
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     *
     * runs to a position in ticks in it's own thread
     *
     * @param power power to run at
     * @param targetposition position to run to
     */
    public void runToPosition(int power, int targetposition) {

        //run to a  position
        this.power = power;
        this.targetPosition = targetposition;
        if(!t.isAlive()) {//make sure only one thread at a time is using this motor
            t.start();
        }
    }


    /**
     *
     * runs to a linear position using encoders and a given radius in it's own thread
     *
     * @param power sets the power to run the motor at
     * @param targetPosition sets a linear position for the motor to run to
     * @param radius radius of the wheel or pulley attached to the wheel
     */
    public void runToPosLinear(double power, double targetPosition, double radius) {

        //run to a linear pos based on a new radius
        this.power = power;
        this.targetPosition = targetPosition * 2 * Math.PI * radius;
        if(!t.isAlive()) {//make sure only one thread at a time is using this motor
            t.start();
        }
    }
    /**
     *
     * runs to a linear position using encoders with a radius already set and stored with the other runToPosLinear or setRadius in it's own thread
     *
     * @param power sets the power to run a motor at
     * @param targetPosition sets a linear position for the motor to run to
     */
    public void runToPosLinear(double power, double targetPosition) {

        //run to a linear position based on a set radius
        this.power = power;
        this.targetPosition = (targetPosition / TICKS_PER_REV) * (2 * Math.PI * radius);
        if(!t.isAlive()) {//make sure only one thread at a time is using this motor
            t.start();
        }
    }

    /**
     * @return linear position of the motor based on the set radius
     */
    public double getLinearPos() {
        //get a linear pos based on a radius
        return (motor.getCurrentPosition() / TICKS_PER_REV) * (2 * Math.PI * radius);
    }

    /**
     * sets the radius of a wheel or pulley attached to a motor for running to positions linearly
     *
     * @param radius radius to set to
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
