package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

class Intakearm extends Mech{

    DcMotor intake;
    DcMotor pivot;
    DigitalChannel upperLimitSwitch;
    DigitalChannel lowerLimitSwitch;

    @Deprecated
    public Intakearm( DcMotor intake, DcMotor pivot, DigitalChannel upperLimitSwitch, DigitalChannel lowerLimitSwitch){
        this.intake = intake;
        this.pivot = pivot;
        this.upperLimitSwitch = upperLimitSwitch;
        this.lowerLimitSwitch = lowerLimitSwitch;
    }

    public Intakearm(){}

    @Override
    public void init(HardwareMap hardwareMap){
        DcMotor intake = hardwareMap.get(DcMotor.class, "Intake Motor");
        DcMotor pivot = hardwareMap.get(DcMotor.class, "Pivot Intake");

        DigitalChannel upperLimit = hardwareMap.get(DigitalChannel.class, "Upper Limit Switch");
        DigitalChannel lowerLimit = hardwareMap.get(DigitalChannel.class, "Lower Limit Switch");

        this.intake = intake;
        this.pivot = pivot;
        this.upperLimitSwitch = upperLimit;
        this.lowerLimitSwitch = lowerLimit;
    }

    @Override
    public void run(){

        if(!upperLimitSwitch.getState() && gamepad.left_bumper){
            lower();
        }
        if (!lowerLimitSwitch.getState() && gamepad.right_bumper) {
            raise();
        }

        if(lowerLimitSwitch.getState()) {
            intake.setPower(gamepad.right_trigger);
            intake.setPower(-gamepad.left_trigger);
        }
    }

    public void lower(){
        long time = System.currentTimeMillis();
        while (!lowerLimitSwitch.getState() && !piviotTimeout(time)){
            pivot.setPower(.75);
        }
    }

    public void raise(){
        long time = System.currentTimeMillis();
        while (!upperLimitSwitch.getState() && !piviotTimeout(time)){
            pivot.setPower(.75);
        }
    }

    private boolean piviotTimeout(long initTime){
        if(System.currentTimeMillis() > initTime+1000){
            pivot.setPower(0);
            return true;
        }else{
            return false;
        }
    }

    public void extend(){
        intake.setPower(1);
    }

    public void retract(){
        intake.setPower(0);
    }

}
