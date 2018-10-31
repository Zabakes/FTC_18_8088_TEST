package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Teleop8088TEST", group="Iterative Opmode")

public class Teleop8088 extends OpMode {

    private SwerveDrive Swerve;
    BNO055IMU imu;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        //imuInit();
        //swerveInit();
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

    private void swerveInit(){
        /*
    The servo center offset is how far from right the servo's zero is
    The default motor direction is positive or negative one depending on which direction the motor turns so the robot goes right while servo is at zero with the offset applied

    wheels numbered
        1-------2
        |       |-> 0 degrees when wheels facing right
        |       |(can be adjusted with servoCenterOffset and defaultMotorDirection)
        4-------3

    */
        final int FRONT_LEFT = 3;
        final int FRONT_RIGHT = 0;
        final int BACK_RIGHT = 1;
        final int BACK_LEFT = 2;

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

        Swerve = new SwerveDrive(swerveModules, imu);
    }

    private void imuInit(){

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!imu.isGyroCalibrated()) {

        }


    }

}