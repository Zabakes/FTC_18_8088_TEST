package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.revextensions2.RevExtensions2;

@TeleOp(name = "Teleop8088", group = "Iterative Opmode")

public class FreeRun extends OpMode {

    public static final double MAX_SERVO_POSITION = 250.0/280.0;//position of the pivot when up scaled from 0-1
    public static final double HOME_SERVO_POSITION = 0/270;//position of the pivot when down scaled from 0-1
    private ElapsedTime runtime = new ElapsedTime();
    public static Mech[] mechs = new Mech[]{new Chassis(16,16) , new Intakearm()};//create an array of the abstract object mech containing all mechanisms on the robot this is possible because all the mechanisms extend the mechs class
    DcMotor slideMotor;
    Servo pivot;
    @Override
    public void init() {

        slideMotor = hardwareMap.get(DcMotor.class, "Output Raise");
        pivot = hardwareMap.get(Servo.class, "Pivot Output");

        RevExtensions2.init();
        for (Mech m : mechs) {
            m.init(hardwareMap); //send a copy of the hardware map out to all the mechanisms
        }
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        Mech.opModeIsactive = true;
        for (Mech m : mechs) {//reads each mech in mechs and calls it's run method
            m.updateAndStart(gamepad1);//send a copy of the gamepad out to all the mechs(m) and run them with that gamepad
            telemetry.addData(m.name(), m);
            telemetry.update();
        }

        if (gamepad1.dpad_up) {
            slideMotor.setPower(1);
        }else if(gamepad1.dpad_down){
            slideMotor.setPower(-1);
        } else {slideMotor.setPower(0);}

        if(gamepad1.a){
            pivot.setPosition(MAX_SERVO_POSITION);
        }else {
            pivot.setPosition(HOME_SERVO_POSITION);
        }


    }

    public static void telemtryAddData(String string){
       // this.telemtry.addData(string);
    }

    @Override
    public void stop() {
        Mech.opModeIsactive = false;
    }
}