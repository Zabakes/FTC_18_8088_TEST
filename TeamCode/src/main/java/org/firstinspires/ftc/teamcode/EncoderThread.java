package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderThread implements Runnable {

    DcMotor motor;

    private int targetposition;
    int power;
    boolean hasrun;

    public EncoderThread(DcMotor motor){
        this.motor = motor;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void run(){

        long t = System.currentTimeMillis();

        double initalpos = motor.getCurrentPosition();

        DcMotor.RunMode mode = motor.getMode();
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor.setTargetPosition(targetposition);
        motor.setPower(power);

        while(motor.isBusy() && System.currentTimeMillis() < t+Math.abs(targetposition - initalpos)*1*power){

        }

        motor.setPower(0);
        motor.setMode(mode);

        }

    public void setTargetposition(int targetposition) {
        this.targetposition = targetposition;
    }


    public void setPower(int power) {
        this.power = power;
    }

    public void runToPosition(int power, int targetposition){
        this.power = power;
        this.targetposition = targetposition;
        this.run();
    }


}
