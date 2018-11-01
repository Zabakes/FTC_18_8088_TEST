package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;


class Chassis extends MechThread{

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

    public Chassis(){}

    @Override
    public void init(HardwareMap hardwareMap){

        DcMotor[] motors = new DcMotor[4];

        final int FRONT_LEFT = 3;
        final int FRONT_RIGHT = 0;
        final int BACK_RIGHT = 1;
        final int BACK_LEFT = 2;

        motors[FRONT_LEFT] = hardwareMap.get(DcMotor.class, "Front Left Motor");
        motors[FRONT_RIGHT] = hardwareMap.get(DcMotor.class, "Front Right Motor");
        motors[BACK_RIGHT] = hardwareMap.get(DcMotor.class, "Back Right Motor");
        motors[BACK_LEFT] = hardwareMap.get(DcMotor.class, "Back Left Motor");

        this.frontLeft = motors[FRONT_LEFT];
        this.frontRight = motors[FRONT_RIGHT];
        this.backRight = motors[BACK_RIGHT];
        this.backLeft = motors[BACK_LEFT];
    }

    @Override
    public void run(){
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
