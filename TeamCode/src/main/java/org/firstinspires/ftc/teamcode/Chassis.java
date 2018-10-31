package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

class Chassis {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public Chassis(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backRight;
        this.backRight = backRight;
    }

    public void run(Gamepad gamepad){
        leftSidePower(-gamepad.left_stick_y);
        rightSidePower(-gamepad.right_stick_y);
    }

    public void leftSidePower(float power){
        frontLeft.setPower(power);
        backLeft.setPower(power);
    }

    public void rightSidePower(float power){
        frontRight.setPower(power);
        backRight.setPower(power);
    }
}
