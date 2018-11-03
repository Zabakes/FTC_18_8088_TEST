package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleop8088TEST", group = "Iterative Opmode")

public class Teleop8088 extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private static Mech[] mechs = new Mech[]{new Chassis(), new Intakearm(), new Outake()};//create an array of the abstract object mech containing all mechanisms on the robot this is possible because all the mechanisms extend the mechs class

    @Override
    public void init() {
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
        for (Mech m : mechs) {//reads each mech in mechs and calls it m
            m.updateAndStart(gamepad1);//send a copy of the gamepad out to all the mechs(m) and run them with that gamepad
        }
    }

    @Override
    public void stop() {
    }

}
