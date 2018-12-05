package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.revextensions2.ExpansionHubMotor;

/**
 * Runs a DC motor to a given position in it's own thread other things can happen while the motor is running to that position
 */
public class EncoderThread implements Runnable {

    DcMotor motor;
    private double targetPosition;
    private double power;
    private double TICKS_PER_REV;
    private double radius = 0;
    Thread t = new Thread(this, "encoder Thread");

    /**
     * @param motor this is the motor that the encoder is attached to
     * @param gearReduction this is is the gear reduction in that motor if a neverest 40 then this is 40
     */
    public EncoderThread(DcMotor motor, double gearReduction) {
        this.motor = motor;
        DcMotor.RunMode mode = motor.getMode();
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TICKS_PER_REV = 7 * gearReduction;
        motor.setMode(mode);
    }

    /**
     * runs to a set position at a set speed with a position set by setTargetPosition and speed set by setPower
     *
     * or called by another method in this class
     */
    @Override
    public void run() {
        try {
            //record all the initial stuff
            long t = System.currentTimeMillis();
            double initialPos = motor.getCurrentPosition();
            DcMotor.RunMode mode = motor.getMode();

            //run to the position
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition((int) Math.round(targetPosition * (power / Math.abs(power))));
            motor.setPower(power);

            //wait to get to the position or timeout
             while (motor.isBusy() && System.currentTimeMillis() < t + Math.abs(targetPosition - initialPos) * power && Mech.opModeIsactive) {

             }

            //turn off and set the mode back to the original
            motor.setPower(0);
            motor.setMode(mode);
        }catch (Exception e){
            Teleop8088.telemtryAddData(e.toString());
        }
    }

    /**
     * sets the target position in ticks for the motor belonging to this object DOES NOT run to that position
     *
     * @param targetPosition target position in ticks
     */
    public boolean setTargetPosition(double targetPosition) {
        if(!t.isAlive()) {
            this.targetPosition = (int) Math.round(targetPosition);
            return true;
        }
        return false;
    }

    /**
     * sets the power for this object DOES NOT run to that position
     *
     * @param power power to set
     */
    public boolean setPower(double power) {
        if (!t.isAlive()){
            this.power = power;
            return true;
        }
        return false;
    }

    /**
     *
     * runs to a position in ticks in it's own thread
     *
     * @param power power to run at
     * @param targetposition position to run to
     */
    public boolean runToPosition(double power, int targetposition) {
        //run to a  position
        if (setPower(power) && setTargetPosition(targetposition)){
            start();
            return true;
        }else
            return false;
    }


    /**
     *
     * runs to a linear position using encoders and a given radius in it's own thread
     *
     * @param power sets the power to run the motor at
     * @param targetPosition sets a linear position for the motor to run to
     * @param radius radius of the wheel or pulley attached to the wheel
     */
    @Deprecated
    public boolean runToPosLinear(double power, double targetPosition, double radius) {
        //run to a linear pos based on a new radius
        if (setPower(power) && setTargetPosition((targetPosition)/((2 * Math.PI * radius)/TICKS_PER_REV))) {
            start();
            return true;
        }else
            return false;

    }
    /**
     *
     * runs to a linear position using encoders with a radius already set and stored with the other runToPosLinear or setRadius in it's own thread
     *
     * @param power sets the power to run a motor at
     * @param targetPosition sets a linear position for the motor to run to
     */
    public boolean runToPosLinear(double power, double targetPosition) {
        //run to a linear position based on a set radius
        if (setPower(power) && setTargetPosition((targetPosition)/((2 * Math.PI * radius)/TICKS_PER_REV))) {
            start();
            return true;
        }else return false;

    }

    /**
     * @return linear position of the motor based on the set radius
     */
    public double getLinearPos() {
        //get a linear pos based on a radius
        try{
            return (motor.getCurrentPosition())/((2 * Math.PI * radius)/TICKS_PER_REV);
         }catch (Exception e) {
            Teleop8088.telemtryAddData(e.toString());
            return (targetPosition);
        }

    }

    /**
     * sets the radius of a wheel or pulley attached to a motor for running to positions linearly
     *
     * @param radius radius to set to
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void start(){
           try{
               t.start();
           }catch (Exception e){
               //run();
               Teleop8088.telemtryAddData(e.toString());
           }
    }
}
