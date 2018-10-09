package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@TeleOp(name="Teleop8088", group="Iterative Opmode")
public class Teleop8088 extends OpMode {

    static final int FRONTLEFT = 0;
    static final int FRONTRIGHT = 1;
    static final int BACKRIGHT = 2;
    static final int BACKLEFT = 3;
                /*
               wheels numbered
                   1-------2
                   |       |
                   |       |
                   4-------3
                */

    SwerveDrive Swerve;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {

        //swerve drive initialization

        SwerveModule[] swerveModules = new SwerveModule[4];
        Servo[] swerveServo = new Servo[4];
        DcMotor[] swerveMotor = new DcMotor[4];

        swerveMotor[FRONTLEFT] = hardwareMap.get(DcMotor.class, "Front Left Motor");
        swerveMotor[FRONTRIGHT] = hardwareMap.get(DcMotor.class, "Front Right Motor");
        swerveMotor[BACKRIGHT] = hardwareMap.get(DcMotor.class, "Back Right Motor");
        swerveMotor[BACKLEFT] = hardwareMap.get(DcMotor.class, "Back Left Motor");

        swerveServo[FRONTLEFT] = hardwareMap.get(Servo.class, "Front Left Servo");
        swerveServo[FRONTRIGHT] = hardwareMap.get(Servo.class, "Front Right Servo");
        swerveServo[BACKRIGHT] = hardwareMap.get(Servo.class, "Back Right Servo");
        swerveServo[BACKLEFT] = hardwareMap.get(Servo.class, "Back Left Servo");

        for(int i = 0; i < swerveModules.length; i++){
            swerveModules[i] = new SwerveModule(swerveMotor[i], swerveServo[i],0 ,1);
        }

        /*TODO add servo offsets and default motor direction to swerve modules*/

        Swerve = new SwerveDrive(swerveModules, gamepad1);

        Swerve.start();

        //swerve drive initialization


    }
    @Override
    public void start() {
        runtime.reset();
        Swerve.DriverControl(true);
    }

    @Override
    public void loop() {

    }
    @Override
    public void stop() {
        Swerve.DriverControl(false);
    }

}
