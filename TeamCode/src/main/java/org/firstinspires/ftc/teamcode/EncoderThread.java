package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderThread implements Runnable{

    DcMotor motor;

    private double targetPosition;
    private double power;
    private double TICKS_PER_REV;
    private double radius;

    public EncoderThread(DcMotor motor, double gearReduction){
        this.motor = motor;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TICKS_PER_REV = 7*gearReduction;
    }

    @Override
    public void run(){

        long t = System.currentTimeMillis();

        double initalpos = motor.getCurrentPosition();

        DcMotor.RunMode mode = motor.getMode();
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor.setTargetPosition((int) Math.round(targetPosition));
        motor.setPower(power);

        while(motor.isBusy() && System.currentTimeMillis() < t+Math.abs(targetPosition - initalpos)*1*power){

        }

        motor.setPower(0);
        motor.setMode(mode);

        }

    public void setTargetposition(int targetPosition) {
        this.targetPosition = targetPosition;
    }


    public void setPower(int power) {
        this.power = power;
    }

    public void runToPosition(int power, int targetposition){
        this.power = power;
        this.targetPosition = targetposition;
        Thread t = new Thread(this);
        t.start();
    }

    public void runToPosLinear(double power, double targetPosition, double radius){
        this.power = power;
        this.targetPosition = targetPosition*2*Math.PI*radius;
        Thread t = new Thread(this);
        t.start();
    }

    public void runToPosLinear(double power, double targetPosition){
        this.power = power;
        this.targetPosition = (targetPosition/TICKS_PER_REV)*(2*Math.PI*radius);
        Thread t = new Thread(this);
        t.start();
    }

    public double getLinearPos(){
        return (motor.getCurrentPosition()/TICKS_PER_REV)*(2*Math.PI*radius);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
