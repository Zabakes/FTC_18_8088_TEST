package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * standard tank style chassis
 */
public class Chassis extends Mech {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor[] motors;
    EncoderThread[] encoders = new EncoderThread[motors.length];

    double width;
    double depth;

    public Chassis(Double width, double depth) {
        this.width = width;
        this.depth = depth;
    }

    /**
     * see init in mech
     *
     * motors named "Front Left Motor" , "Front Right Motor" , "Back Right Motor" , "Back Left Motor"
     * can you guess which motor's which ?????????????
     *
     * @param hardwareMap hardwaremap from the phone
     */
    @Override
    public void init(HardwareMap hardwareMap) {

        //initialize the chassis

        frontLeft = hardwareMap.get(DcMotor.class, "Front Left Motor");
        frontRight = hardwareMap.get(DcMotor.class, "Front Right Motor");
        backRight = hardwareMap.get(DcMotor.class, "Back Right Motor");
        backLeft = hardwareMap.get(DcMotor.class, "Back Left Motor");

        motors = new DcMotor[]{frontLeft,frontRight,backLeft,backRight};


        int i = 0;
        for (DcMotor m:motors) {
            encoders[i] = new EncoderThread(m, 40);
            encoders[i].setRadius(3);
            i++;
        }

    }

    /**
     * run the chassis in a tank style
     */
    @Override
    public void run() {
        //run tank
        leftSidePower(-gamepad.left_stick_y);
        rightSidePower(gamepad.right_stick_y);
    }

    /**
     * set the and send a power to the motors on the the left side of the chassis
     * @param power power to set
     */
    public void leftSidePower(float power) {
        if(opModeIsactive) {
            frontLeft.setPower(power);
            backLeft.setPower(power);
        }
    }
    /**
     * set the and send a power to the motors on the the right side of the chassis
     * @param power power to set
     */
    public void rightSidePower(float power) {
        if(opModeIsactive) {
            frontRight.setPower(power);
            backRight.setPower(power);
        }
    }

   public void go(float power , double distance){
        if(opModeIsactive) {
            for (EncoderThread motor : encoders) {
                motor.runToPosLinear(power, distance);
            }
        }
   }

   public void turn(float power, double degrees){

        if (opModeIsactive) {
            double wheelCircleCircumfrence = 2 * Math.PI * Math.sqrt(Math.pow(width / 2, 2) + Math.pow(depth / 2, 2));
            double distToTurn = wheelCircleCircumfrence * 360 / degrees;
            encoders[1].runToPosLinear(power, distToTurn);
            encoders[2].runToPosLinear(-power, distToTurn);
            encoders[3].runToPosLinear(power, distToTurn);
            encoders[4].runToPosLinear(-power, distToTurn);
        }
   }
}
