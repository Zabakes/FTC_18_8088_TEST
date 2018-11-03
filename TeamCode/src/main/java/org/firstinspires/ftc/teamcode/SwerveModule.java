package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 *     The servo center offset is how far from right the servo's zero is
 *     The default motor direction is positive or negative one depending on which direction the motor turns so the robot goes right while servo is at zero with the offset applied
 *
 *   0 degrees when wheels facing right ->(that way)
 *
 *         this is an object that represents each module
 */
public class SwerveModule {

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
    private double motorDirection;
    private int defaultMotorDirection;
    private int servoZeroOffset;//how far behind zero the servo's zero is
    private double currentAngle;

    private static double TURNING_GEAR_RATIO = 23.00 / 30.00;
    private static double MAX_SERVO_TRAVEL_DEGREES = 270;

    /**
     *
     * constructor for each swerve module
     *
     * @param driveMotor motor attached to the wheel
     * @param turningMotor servo that controls the rotation
     * @param servoZeroOffset how far behind right the servo's zero is
     * @param defaultMotorDirection + or - so that the motor makes the robot go right when the servo is at zero+offset
     */
    public SwerveModule(DcMotor driveMotor, Servo turningMotor, int servoZeroOffset, int defaultMotorDirection) {
        this.driveMotor = driveMotor;
        this.turningMotor = turningMotor;
        this.defaultMotorDirection = Math.abs(defaultMotorDirection) / defaultMotorDirection;//make default motor direction +/-1
        this.servoZeroOffset = servoZeroOffset;
    }

    /**
     *
     * puts the angle in the servo's range if the angle is in a position that the servo can't reach it will just go as close as possible
     *
     * @param angle angle for the module to go to
     */
    public void normalizeAndSetAngle(double angle) {

        //normalize angle
        motorDirection = defaultMotorDirection;

        while (angle < 0) {
            angle += 360;
        }

        while (angle > (MAX_SERVO_TRAVEL_DEGREES * TURNING_GEAR_RATIO) || angle > 2 * Math.PI) {
            if (angle >= 180) {
                motorDirection *= -1;
                angle -= 180;
            } else {
                angle = (MAX_SERVO_TRAVEL_DEGREES * TURNING_GEAR_RATIO);
            }
        }

        currentAngle = angle;

        //move to angle
        turningMotor.setPosition((((angle + servoZeroOffset)/TURNING_GEAR_RATIO)/360));
    }

    public double getAngle() {
        return currentAngle;
    }

    /**
     * speed for the wheel to turn
     * @param speed speed between 1-0
     */
    public void setSpeed(double speed) {
        driveMotor.setPower(speed * motorDirection);
    }

    /**
     * sets the servo's zero offset outside of the constructor
     *
     * @param servoZeroOffset how far behind right the servo's zero is
     */
    public void setServoCenterOffset(int servoZeroOffset) {
        this.servoZeroOffset = servoZeroOffset;
    }

    /**
     * sets the default motor direction outside of the constructor
     * @param defaultMotorDirection + or - so that the motor makes the robot go right when the servo is at zero+offset
     */
    public void setDefaultMotorDirection(int defaultMotorDirection) {
        this.defaultMotorDirection = defaultMotorDirection;
    }
}
