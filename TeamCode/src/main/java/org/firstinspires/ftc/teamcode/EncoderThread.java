package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderThread extends Thread {

    DcMotor motor;

    int position;
    int power;

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

        motor.setTargetPosition(position);
        motor.setPower(power);

        while(motor.isBusy() && System.currentTimeMillis() < t+Math.abs(position - initalpos)*1*power){

        }

        motor.setPower(0);
        motor.setMode(mode);

        }

    public void setPosition(int position) {
        this.position = position;
    }


    public void setPower(int power) {
        this.power = power;
    }

}
