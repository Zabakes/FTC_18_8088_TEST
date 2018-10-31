package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class SwerveModule{

    /*
    The servo center offset is how far from right the servo's zero is
    The default motor direction is positive or negative one depending on which direction the motor turns so the robot goes right while servo is at zero with the offset applied

    wheels numbered
        1-------2
        |       |-> 0 degrees when wheels facing right
        |       |(can be adjusted with servoCenterOffset and defaultMotorDirection)
        4-------3

    */

    private DcMotor driveMotor;
    private Servo turningMotor;
    private double turnigGearRatio = 23.00/30.00;
    private double maxServoTravelDegrees = 3/2*Math.PI;
    private double motorDirection;
    private int defaultMotorDirection;
    private int servoCenterOffset;
    private double currentAngle;


    public SwerveModule(DcMotor driveMotor, Servo turningMotor, int servoCenterOffset, int defaultMotorDirection){
        this.driveMotor = driveMotor;
        this.turningMotor = turningMotor;
        this.defaultMotorDirection = Math.abs(defaultMotorDirection/defaultMotorDirection);
        this.servoCenterOffset = servoCenterOffset;
    }

    public void setWheelPosition(double angle){
        motorDirection = defaultMotorDirection;

        while(angle < 0 ){
            angle += 2*Math.PI;
        }

        while (angle > ((maxServoTravelDegrees)*turnigGearRatio) || angle > 2*Math.PI) {
            if(angle >= Math.PI) {
                motorDirection *= -1;
                angle -= Math.PI;
            }else{
                angle = ((maxServoTravelDegrees)*turnigGearRatio);
            }
        }

        currentAngle = angle;
        turningMotor.setPosition((((angle+servoCenterOffset)-(maxServoTravelDegrees/2))/turnigGearRatio)/2*Math.PI);
    }

    public double getAngle() {
        return currentAngle;
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
