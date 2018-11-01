package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Teleop8088TEST", group="Iterative Opmode")

public class Teleop8088 extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private static Mech[] mechs = new Mech[]{new Chassis(),new Intakearm(),new Outake()};

    @Override
    public void init() {
        for (Mech m:mechs) {
            m.init(hardwareMap);
        }
    }
    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        for (Mech m:mechs) {
            m.updateAndStart(gamepad1);
        }
    }
    @Override
    public void stop() {
    }

}
