package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Teleop8088", group="Iterative Opmode")

public class Teleop8088 extends OpMode {

    static final int FRONT_LEFT = 3;
    static final int FRONT_RIGHT = 0;
    static final int BACK_RIGHT = 1;
    static final int BACK_LEFT = 2;
                /*
               wheels numbered with front being navX +x
                   4-------1
                   |       |
                   |       |
                   3-------2
                */

    private SwerveDrive Swerve;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {

        //swerve drive initialization

        SwerveModule[] swerveModules = new SwerveModule[4];
        Servo[] swerveServo = new Servo[4];
        DcMotor[] swerveMotor = new DcMotor[4];

        swerveMotor[FRONT_LEFT] = hardwareMap.get(DcMotor.class, "Front Left Motor");
        swerveMotor[FRONT_RIGHT] = hardwareMap.get(DcMotor.class, "Front Right Motor");
        swerveMotor[BACK_RIGHT] = hardwareMap.get(DcMotor.class, "Back Right Motor");
        swerveMotor[BACK_LEFT] = hardwareMap.get(DcMotor.class, "Back Left Motor");

        swerveServo[FRONT_LEFT] = hardwareMap.get(Servo.class, "Front Left Servo");
        swerveServo[FRONT_RIGHT] = hardwareMap.get(Servo.class, "Front Right Servo");
        swerveServo[BACK_RIGHT] = hardwareMap.get(Servo.class, "Back Right Servo");
        swerveServo[BACK_LEFT] = hardwareMap.get(Servo.class, "Back Left Servo");

        for(int i = 0; i < swerveModules.length; i++){
            swerveModules[i] = new SwerveModule(swerveMotor[i], swerveServo[i],0 ,1);
        }

        /*TODO add servo offsets and default motor direction to swerve modules*/

        Swerve = new SwerveDrive(swerveModules);

        //swerve drive initialization


    }
    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        Swerve.run(gamepad1);
    }
    @Override
    public void stop() {
    }

}
