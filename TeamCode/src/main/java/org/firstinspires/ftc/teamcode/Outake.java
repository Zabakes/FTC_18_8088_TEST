package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * all the code for the outake/output of 8088's 2018 robot
 */
public class Outake extends Mech {
    private DcMotor raiseMotor;
    private Servo pivot;
    private EncoderThread raiseMotorEncoder;

    //TODO set these
    public static final double MAX_LINEAR_TRAVEL = 15.75;//max linear travel in inches of the slide
    public static final double MAX_SERVO_POSITION = 250.0/280.0;//position of the pivot when up scaled from 0-1
    public static final double HOME_SERVO_POSITION = 0/270;//position of the pivot when down scaled from 0-1
    public static final double WHEEL_RADIUS = 1.325;//radius of the wheel the string is wrapped around
    public static final double CLIMB_HEIGHT =  10;


    /**
     * see init in mech
     *
     * motor called "Output Raise" for sliding
     * servo called "Pivot Output"  for you guessed it pivoting
     *
     * @param hardwareMap hardwaremap from the phone
     */
    @Override
    public void init(HardwareMap hardwareMap) {
        DcMotor slideMotor = hardwareMap.get(DcMotor.class, "Output Raise");
        Servo pivot = hardwareMap.get(Servo.class, "Pivot Output");

        this.raiseMotor = slideMotor;
        this.pivot = pivot;

        raiseMotorEncoder = new EncoderThread(raiseMotor, 40);
        raiseMotorEncoder.setRadius(WHEEL_RADIUS);
    }

    /**
     * run in teleop if the a button is pressed it will raise and dump objects otherwise it will retract and return home if the b button is pressed it will attempt to climb
     */
    @Override
    public void run() {
        if (gamepad.a) {
            raise();
            dump();
        }else{
            unDump();
            lower();
        }
        if (gamepad.b) {
            climb();
        }
    }
    /**
     * go to climb height then back down
     */
    private void climb() {
            raiseMotorEncoder.runToPosLinear(.75, CLIMB_HEIGHT);
            raiseMotorEncoder.runToPosLinear(.75, 0);
    }


    /**
     * @return the height is inches above home that the mechanism currently is
     */
    public double howHigh() {
        return raiseMotorEncoder.getLinearPos();
    }

    /**
     *dump objects out of the basket
     */
    public void dump() {
            pivot.setPosition(MAX_SERVO_POSITION);
    }
    /**
     *retract the basket
     */
    public void unDump() {
            pivot.setPosition(HOME_SERVO_POSITION);
    }

    /**
     * lower the slide
     */
    public void lower() {
            raiseMotorEncoder.runToPosLinear(1, 1);
    }

    /**
     * raise the slide
     */
    public void raise() {
        raiseMotorEncoder.runToPosLinear(1, MAX_LINEAR_TRAVEL);
    }

    public void unClimb(){
        raiseMotorEncoder.runToPosLinear(1, CLIMB_HEIGHT+3);
    }

    @Override
    public String name(){
        return "outptut";
    }

}
