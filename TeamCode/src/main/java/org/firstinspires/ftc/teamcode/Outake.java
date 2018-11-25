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

    //TODO
    public static final double MAX_LINEAR_TRAVEL = 15.75;//max linear travel in inches of the slide
    public static final double MAX_SERVO_POSITION = 1/180;//position of the pivot when up scaled from 0-1
    public static final double HOME_SERVO_POSITION = 1/180;//position of the pivot when down scaled from 0-1
    public static final double WHEEL_RADIUS = 1.325;//radius of the wheel the string is wrapped around
    public static final double CLIMB_HEIGHT =  5;

    public Outake() {
    }

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
            if (!isUp()) {
                raise();
            } else {
                dump();
            }
        } else {
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
        if (opModeIsactive) {
            raiseMotorEncoder.runToPosLinear(.75, CLIMB_HEIGHT);
            raiseMotorEncoder.runToPosLinear(.75, 0);
        }
    }

    /**
     * @return is the output all the way up
     */
    public boolean isUp() {
        return raiseMotorEncoder.getLinearPos() >= MAX_LINEAR_TRAVEL;
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
        if(opModeIsactive)
            pivot.setPosition(MAX_SERVO_POSITION);
    }
    /**
     *retract the basket
     */
    public void unDump() {
        if(opModeIsactive)
            pivot.setPosition(HOME_SERVO_POSITION);
    }

    /**
     * lower the slide
     */
    public void lower() {
        if(opModeIsactive)
            raiseMotorEncoder.runToPosLinear(1, 0);
    }

    /**
     * raise the slide
     */
    public void raise() {
        if(opModeIsactive)
            raiseMotorEncoder.runToPosLinear(1, MAX_LINEAR_TRAVEL);
    }

    public void unClimb(){
        if(opModeIsactive)
            raiseMotorEncoder.runToPosLinear(1, CLIMB_HEIGHT+3);
    }
}
