package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * all the code for the intake arm opn 8088's 2018 robot
 */
public class Intakearm extends Mech {//a mechanism intake arm

    private DcMotor intake;
    private DcMotor pivot;
    private DigitalChannel upperLimitSwitch;
    private DigitalChannel lowerLimitSwitch;

    public Intakearm() {
    }//constructor for intake arm

    /**
     * see init in mech
     *
     * intake motor called "Inatke Motor" :o
     * pivot motor called "Pivot Intake" ik I'm crazy
     * limit switch triggered when the intake is in called "Upper limit Switch"
     * limit switch triggered when the intake is out called "Lower limit Switch"
     *
     * @param hardwareMap hardwaremap from the phone
     */
    @Override
    public void init(HardwareMap hardwareMap) {//initalize the intake arm

        intake = hardwareMap.get(DcMotor.class, "Intake Motor");//get the intake motor from the passed the copy of the hardware map
        pivot = hardwareMap.get(DcMotor.class, "Pivot Intake");//get the pivot motor on the intake from the passed the copy of the hardware map

        upperLimitSwitch = hardwareMap.get(DigitalChannel.class, "Upper Limit Switch");//get the upper limit switch from the passed copy of the hardware map
        lowerLimitSwitch = hardwareMap.get(DigitalChannel.class, "Lower Limit Switch");//get the lower limit switch from the passed copy of the hardware map

    }

    @Override
    public void run() {

        if (!upperLimitSwitch.getState() && gamepad.left_bumper) {//if not already raised or tucked (determined with the lower limit switch) raise the intake when the left bumper is pressed
            raise();
        }
        if (!lowerLimitSwitch.getState() && gamepad.right_bumper) {//if not already lowered (determined with the lower limit switch) lower the intake when the right bumper is pressed
            lower();
        }

        if (lowerLimitSwitch.getState()) {
            intake.setPower(gamepad.right_trigger);//runs the out intake at a power determined by the right trigger
            intake.setPower(-gamepad.left_trigger);//retracts the intake at a power determined by the left trigger
        }


    }

    public void lower() {//lowers the arm by running the motor until the lower limit switch is triggered
        long time = System.currentTimeMillis();//store the time that this method was called
        while (!lowerLimitSwitch.getState() && !piviotTimeout(time)) {//make sure that arm is not up (through the limit switch) and that the motor is'nt stuck (by passing the time the method was called to pivot timeout). if both conditions are met run the motor up.
            pivot.setPower(-.75);
        }
        pivot.setPower(0);//once either the limit switch is triggered or the action has timed out turn off the motor
    }

    public void raise() {//raises or tucks the arm back in by running the motor until the upper limit switch is triggered
        long time = System.currentTimeMillis();//store the time that this method was called
        while (!upperLimitSwitch.getState() && !piviotTimeout(time)) {
            pivot.setPower(.75);
        }
        pivot.setPower(0);//once either the limit switch is triggered or the action has timed out turn off the motor
    }

    private boolean piviotTimeout(long initTime) { // takes the time the arm started moving and checks that the arm has only been running for 1000ms
        if (System.currentTimeMillis() > initTime + 1000) {//make sure that arm is not up (through the limit switch) and that the motor is'nt stuck (by passing the time the method was called to pivot timeout). if both conditions are met run the motor up.
            pivot.setPower(0);
            return true;//if it's been running for too long return true it has timed out
        } else {
            return false;
        }
    }

    public void extend() {//extends and intakes at full power
        intake.setPower(1);
    }

    public void retract() {//retracts the intake at full power
        intake.setPower(0);
    }

}
