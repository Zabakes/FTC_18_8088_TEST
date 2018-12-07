package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.revextensions2.RevExtensions2;

@TeleOp(name = "Teleop8088Test", group = "Iterative Opmode")

public class Teleop8088 extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    static String toPrint;
    public static Mech[] mechs = new Mech[]{new Outake(), new Intakearm(), new Chassis(16, 16)};//create an array of the abstract object mech containing all mechanisms on the robot this is possible because all the mechanisms extend the mechs class

    @Override
    public void init() {
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

        }

        telemetry.addData(toPrint, this);
        toPrint = "";
        telemetry.update();
        telemetry.clearAll();

    }

    public static void telemtryAddData(String string){
        toPrint = toPrint + string;
    }

    @Override
    public void stop() {
       Mech.opModeIsactive = false;
    }

}

