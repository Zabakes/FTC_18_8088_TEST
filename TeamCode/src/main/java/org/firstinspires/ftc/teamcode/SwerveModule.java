package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class SwerveModule{

    /*
    The servo center offset is how far from "forward" the servo's zero is
    The default motor direction is positive or negitive one depending on which direction the motor turns so the robot can go forward

    wheels numbered
        1-------2
        |       |
        |       |
        4-------3

    */

    private DcMotor driveMotor;
    private Servo turningMotor;
    private double turnigGearRatio = 23.00/30.00;
    private double maxServoTravelDegrees = 270;
    private double motorDirection;
    private int defaultMotorDirection;
    private int servoCenterOffset;
    private double angle;


    public SwerveModule(DcMotor driveMotor, Servo turningMotor, int servoCenterOffset, int defaultMotorDirection){
        this.driveMotor = driveMotor;
        this.turningMotor = turningMotor;
        this.defaultMotorDirection = defaultMotorDirection;
        this.servoCenterOffset = servoCenterOffset;
    }

    public void setWheelPosition(double angle){
        motorDirection = defaultMotorDirection;

        while(angle < 0 ){
            angle += 360;
        }

        while (angle > ((maxServoTravelDegrees)*turnigGearRatio) || angle > 360) {
            if(angle >= 180) {
                motorDirection *= -1;
                angle -= 180;
            }else{
                angle = ((maxServoTravelDegrees)*turnigGearRatio);
            }
        }

        this.angle = angle;
        turningMotor.setPosition((((angle+servoCenterOffset)-(maxServoTravelDegrees/2))/turnigGearRatio)/360);
    }

    public double getAngle() {
        return angle;
    }

    public void Go(double speed){
        driveMotor.setPower(speed*motorDirection);
    }

    public void setServoCenterOffset(int servoCenterOffset) {
        this.servoCenterOffset = servoCenterOffset;
    }

    public void setDefaultMotorDirection(int defaultMotorDirection) {
        this.defaultMotorDirection = defaultMotorDirection;
    }
}
