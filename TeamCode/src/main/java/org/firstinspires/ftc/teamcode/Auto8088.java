package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "auto8080TEST", group = "Iterative Opmode")
public class Auto8088 extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();
    private Intakearm intake = new Intakearm();
    private Chassis chassis = new Chassis();
    private Outake outake = new Outake();


    @Override
    public void runOpMode() {

        intake.init(hardwareMap);
        chassis.init(hardwareMap);
        outake.init(hardwareMap);

        waitForStart();

        outake.unClimb();
        chassis.go(1, -5);
        outake.lower();

    }

}
