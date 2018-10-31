package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;

class Intakearm {

    Gamepad gamepad;
    DcMotor intake;
    //DcMotor pullBack;
    DcMotor piviot;
    DigitalChannel upperLimitSwitch;
    DigitalChannel lowerLimitSwitch;

    public Intakearm( DcMotor intake, DcMotor piviot, DigitalChannel upperLimitSwitch, DigitalChannel lowerLimitSwitch){
        this.intake = intake;
        //this.pullBack = pullBack;
        this.piviot = piviot;
        this.upperLimitSwitch = upperLimitSwitch;
        this.lowerLimitSwitch = lowerLimitSwitch;
    }

    public void run(Gamepad gamepad){

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
