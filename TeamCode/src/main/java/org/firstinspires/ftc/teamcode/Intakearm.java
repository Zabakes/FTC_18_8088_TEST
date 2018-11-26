package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.revextensions2.ExpansionHubMotor;

/**
 * all the code for the intake arm opn 8088's 2018 robot
 */
public class Intakearm extends Mech {//a mechanism intake arm

    private DcMotor intake;
    private ExpansionHubMotor pivot;
    private DigitalChannel upperLimitSwitch;
    private DigitalChannel lowerLimitSwitch;
    private Double MOTOR_STALL_CURRENT = 11500.0;

    boolean isUp = true;
    boolean isDown = false;

    public Intakearm() {
    }//constructor for intake arm

    /**
     * see init in mech
     *
     * intake motor called "Inatke Motor" :o
     * pivot motor called "Pivot Intake"
     *
     * limit switches replaced with current detection
     *
     * @param hardwareMap hardwaremap from the phone
     */
    @Override
    public void init(HardwareMap hardwareMap) {//initalize the intake arm

        intake = hardwareMap.get(DcMotor.class, "Intake Motor");//get the intake motor from the passed the copy of the hardware map
        pivot = (ExpansionHubMotor)hardwareMap.get(DcMotor.class, "Pivot Intake");//get the pivot motor on the intake from the passed the copy of the hardware map
    }

    @Override
    public void run() {
        if(opModeIsactive) {
            if (gamepad.left_bumper) {
                lower();
            }
            if (gamepad.right_bumper) {
                raise();
            }
            if (isDown) {
                intake.setPower(gamepad.right_trigger);//runs the out intake at a power determined by the right trigger
                intake.setPower(-gamepad.left_trigger);//retracts the intake at a power determined by the left trigger
            }
        }

        }


        public void lower () {//lowers the arm by running the motor until the lower limit switch is triggered
            long time = System.currentTimeMillis();//store the time that this method was called

            isUp = false;

            do{
                pivot.setPower(-.75);
                if(piviotTimeout(time)){
                    return;
                }
            }while(pivot.getCurrentDraw() < MOTOR_STALL_CURRENT && opModeIsactive);

            isDown = true;
            pivot.setPower(0);//once either the limit switch is triggered or the action has timed out turn off the motor
        }

        public void raise (){//raises or tucks the arm back in by running the motor until the upper limit switch is triggered
            long time = System.currentTimeMillis();//store the time that this method was called

            isDown = false;
            
            do{
                pivot.setPower(.75);
                if(piviotTimeout(time)){
                    return;
                }
            }while(pivot.getCurrentDraw() < MOTOR_STALL_CURRENT && opModeIsactive);

            isUp = true;
            pivot.setPower(0);//once either the limit switch is triggered or the action has timed out turn off the motor
        }

        private boolean piviotTimeout ( long initTime)
        { // takes the time the arm started moving and checks that the arm has only been running for 1000ms
            return  (System.currentTimeMillis() > initTime + 1000);//test for timeout
        }



        public void extend () {//extends and intakes at full power
            intake.setPower(1);
        }

        public void retract () {//retracts the intake at full power
            intake.setPower(0);
        }


}
