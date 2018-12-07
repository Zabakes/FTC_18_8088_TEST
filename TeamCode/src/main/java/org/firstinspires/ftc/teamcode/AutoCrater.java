package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto Crater", group = "Iterative Opmode")
public class AutoCrater extends LinearOpMode{

    @Override
    public void runOpMode(){
        Auto8088.init(hardwareMap);
        waitForStart();
        Mech.opModeIsactive = true;
        Auto8088.runOpMode(false);
        Mech.opModeIsactive = false;
    }

}
