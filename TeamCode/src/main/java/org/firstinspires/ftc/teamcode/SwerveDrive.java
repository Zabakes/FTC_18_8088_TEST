package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/*
    The servo center offset is how far from right the servo's zero is
    The default motor direction is positive or negative one depending on which direction the motor turns so the robot goes right while servo is at zero with the offset applied

    wheels numbered
        1-------2
        |       |-> 0 degrees when wheels facing right
        |       |(can be adjusted with servoCenterOffset and defaultMotorDirection)
        4-------3

*/

public class SwerveDrive extends Mech{

    public SwerveModule[] swerveModules;
    private double angle;
    private double speed;
    private NavxMicroNavigationSensor navx = null;
    private BNO055IMU imu = null;

    @Override
    public void init(HardwareMap hardwareMap) {
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

        swerveModules = new SwerveModule[4];
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

        if(hardwareMap.tryGet(BNO055IMU.class, "IMU") != null){

                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

                parameters.mode                = BNO055IMU.SensorMode.IMU;
                parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
                parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                parameters.loggingEnabled      = false;

                // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
                // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
                // and named "imu".
                imu = hardwareMap.get(BNO055IMU.class, "IMU");

                imu.initialize(parameters);

                // make sure the imu gyro is calibrated before continuing.
                while (!imu.isGyroCalibrated()) {

                }


            }


        if(hardwareMap.tryGet(NavxMicroNavigationSensor.class, "NavX") != null){
            navx = hardwareMap.get(NavxMicroNavigationSensor.class, "NavX");
        }

        for(int i = 0; i < swerveModules.length; i++){
            swerveModules[i] = new SwerveModule(swerveMotor[i], swerveServo[i],0 ,1);
        }

        /*TODO add servo offsets and default motor direction to swerve modules*/


    }

    @Deprecated
    public SwerveDrive(SwerveModule[] swerveModules) {
        this.swerveModules = swerveModules;
    }

    @Deprecated
    public SwerveDrive(SwerveModule[] swerveModules, NavxMicroNavigationSensor navx) {
        this.swerveModules = swerveModules;
        this.navx = navx;
    }

    @Deprecated
    public SwerveDrive(SwerveModule[] swerveModules, BNO055IMU imu) {
        this.swerveModules = swerveModules;
        this.imu = imu;
    }

    public SwerveDrive() {
    }

    public void run(){

            double STR = gamepad.left_stick_x;
            double FWD = -gamepad.left_stick_y;
            double RCW = gamepad.right_stick_x;

            double firstModuleAngle = 45;
            double joystickMagnitude = Math.sqrt(Math.pow(STR,2)+Math.pow(FWD , 2));
            double joystickAngle = Math.atan2(FWD, STR);
            double navxOffset = 0;

            if(navx != null) {
                navxOffset += navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
            }else if(imu != null) {
            navxOffset += imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
            }

            // write the speed and angles to all modules
            for(int i = 0; i < swerveModules.length; i++){

                firstModuleAngle += (360/swerveModules.length)*i;

                double xComponent = Math.sin(Math.toRadians(firstModuleAngle))*RCW+Math.cos(joystickAngle-navxOffset)*joystickMagnitude;
                double yComponent = Math.cos(Math.toRadians((firstModuleAngle)))*RCW+Math.sin(joystickAngle-navxOffset)*joystickMagnitude;

                swerveModules[i].setWheelPosition(Math.toDegrees(Math.atan2(yComponent, xComponent)));
                swerveModules[i].Go((Math.sqrt(Math.pow(yComponent, 2)+Math.pow(xComponent, 2))/2));

            }
        }

        public void setUniversalAngle(double angle){
            this.angle = angle;
        }

        public void setUniversalSpeed(double speed){
            this.speed = speed;
        }

        public double getAngle (){
            return angle;
        }

        public void auto(float autoStrafe, float autoFwd, float autoRotate) {
            gamepad.right_stick_x = autoRotate;
            gamepad.left_stick_x = autoStrafe;
            gamepad.left_stick_y = autoFwd;
        }

    }