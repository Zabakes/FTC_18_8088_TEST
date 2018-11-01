package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

class Intakearm extends MechThread{

    DcMotor intake;
    DcMotor piviot;
    DigitalChannel upperLimitSwitch;
    DigitalChannel lowerLimitSwitch;

    public Intakearm( DcMotor intake, DcMotor piviot, DigitalChannel upperLimitSwitch, DigitalChannel lowerLimitSwitch){
        this.intake = intake;
        this.piviot = piviot;
        this.upperLimitSwitch = upperLimitSwitch;
        this.lowerLimitSwitch = lowerLimitSwitch;
    }

    public Intakearm(){}

    @Override
    public void init(HardwareMap hardwareMap){
        DcMotor intake = hardwareMap.get(DcMotor.class, "Intake Motor");
        DcMotor piviot = hardwareMap.get(DcMotor.class, "Pivot Intake");

        DigitalChannel upperLimit = hardwareMap.get(DigitalChannel.class, "Upper Limit Switch");
        DigitalChannel lowerLimit = hardwareMap.get(DigitalChannel.class, "Lower Limit Switch");

        this.intake = intake;
        this.piviot = piviot;
        this.upperLimitSwitch = upperLimit;
        this.lowerLimitSwitch = lowerLimit;
    }

    @Override
    public void run(){

        if(!upperLimitSwitch.getState() && gamepad.left_bumper){
            piviot.setPower(.75);
        }
        if (!lowerLimitSwitch.getState() && gamepad.right_bumper) {
            piviot.setPower(.75);
        }

        intake.setPower(gamepad.right_trigger);
        intake.setPower(-gamepad.left_trigger);
    }
}
