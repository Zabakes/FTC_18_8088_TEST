package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto Depot", group = "Iterative Opmode")
public class AutoDepot extends LinearOpMode {

    @Override
    public void runOpMode(){
        Auto8088.init(hardwareMap);
        waitForStart();
        Auto8088.runOpMode(false);
    }
}
