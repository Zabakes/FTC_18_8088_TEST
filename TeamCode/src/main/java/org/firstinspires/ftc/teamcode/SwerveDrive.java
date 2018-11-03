package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
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
/**
 * swerve drive chassis
 */
public class SwerveDrive extends Mech {

    public SwerveModule[] swerveModules;
    private double angle;
    private double speed;
    private NavxMicroNavigationSensor navx = null;
    private BNO055IMU imu = null;

    /**
     * see init in mechs
     *
     * The servo center offset is how far from right the servo's zero is
     * the default motor direction is positive or negative one depending on which direction the motor turns so the robot goes right while servo is at zero with the offset applied
     * 0 degrees when wheels facing right ->(that way)
     * can be adjusted with setServoCenterOffset and setDefaultMotorDirection called in each module's object
     *
     *
     * motors named "Front Left Motor", "Front Right Motor", "Back Right Motor", "Back Left Motor"
     *servos named "Front Left Servo", "Front Right Servo", "Back Right Servo, "Back Left Servo"
     *can you guess what module each one corresponds to :)
     *
     * optional imu called "IMU" or Navx named "NavX"
     * @param hardwareMap hardwaremap from the phone
     */
    @Override
    public void init(HardwareMap hardwareMap) {

        final int FRONT_LEFT = 3;
        final int FRONT_RIGHT = 0;
        final int BACK_RIGHT = 1;
        final int BACK_LEFT = 2;


        //initalize all the swerve modules including their motors and servos
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

        //try to initialize an imu
        if (hardwareMap.tryGet(BNO055IMU.class, "IMU") != null) {

            imu = hardwareMap.get(BNO055IMU.class, "IMU");

            if(imu.isGyroCalibrated() != true) {

                //set params for imu
                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

                parameters.mode = BNO055IMU.SensorMode.IMU;
                parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
                parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                parameters.loggingEnabled = false;

                imu.initialize(parameters);

                // make sure the imu gyro is calibrated before continuing.
                while (!imu.isGyroCalibrated()) {

                }
            }


        }

            //try to initialize a navX
            navx = hardwareMap.tryGet(NavxMicroNavigationSensor.class, "NavX");

        for (int i = 0; i < swerveModules.length; i++) {//send all the motors and servos to their respective modules
            swerveModules[i] = new SwerveModule(swerveMotor[i], swerveServo[i], 0, 1);
        }

        //TODO add servo offsets and default motor direction to swerve modules*/

    }

    public SwerveDrive() {
    }

    /**
     * run method for swerve drive uses left stick for direction and right stick horizontal for rotation.
     * if a navx or imu exists it will use that and be field centric see init for details on what to call these.
     */
    @Override
    public void run() {

        //set the joysticks to their direction
        double STR = gamepad.left_stick_x;
        double FWD = -gamepad.left_stick_y;
        double RCW = gamepad.right_stick_x;


        double moduleRotAngle = 45;//the angle of the first module relative to horizontal zero
        double joystickMagnitude = Math.sqrt(Math.pow(STR, 2) + Math.pow(FWD, 2));//magnitude of the non rotational joystick's vector
        double joystickAngle = Math.atan2(FWD, STR);//angle of the non rotational vector
        double navxOffset = 0;//offset of the accelerometer

        //if an accelerometer exists get it's angle to make movement field centric
        if (navx != null) {
            navxOffset += navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
        } else if (imu != null) {
            navxOffset += imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
        }

        // write the speed and angles to all modules
        for (int i = 0; i < swerveModules.length; i++) {

            moduleRotAngle += (360 / swerveModules.length) * i;//angle for the module to rotate

            double xComponent = Math.sin(Math.toRadians(moduleRotAngle)) * RCW + Math.cos(joystickAngle - navxOffset) * joystickMagnitude;//total x component of the module's vector
            double yComponent = Math.cos(Math.toRadians((moduleRotAngle))) * RCW + Math.sin(joystickAngle - navxOffset) * joystickMagnitude;//total y component of the module's vector

            swerveModules[i].normalizeAndSetAngle(Math.toDegrees(Math.atan2(yComponent, xComponent)));//send the angle in degrees to the module
            swerveModules[i].setSpeed((Math.sqrt(Math.pow(yComponent, 2) + Math.pow(xComponent, 2)) / 2));//send the speed from 0-1 to the module

        }
    }

    /**
     * runs all swerve modules to angle good for auto maybe
     * @param angle angle to run to again 0 is right
     */
    public void setUniversalAngle(double angle) {
        this.angle = angle;
        for (SwerveModule s: swerveModules) {
            s.normalizeAndSetAngle(angle);
        }
    }
    /**
     * runs all swerve modules at a speed good for auto maybe
     * @param speed speed to run at from 0-1
     */
    public void setUniversalSpeed(double speed) {
        this.speed = speed;
        for (SwerveModule s: swerveModules) {
            s.setSpeed(speed);
        }
    }

    /**
     *
     * runs swerve in auto based off a spoofed gamepad and the run method
     *
     * respects isThread from mechs so has the ability to run on it's own thread
     *
     * @param autoStrafe strafe/horizontal/^ competent of the chassis vector
     * @param autoFwd forward/vertical/> competent of the chassis vector
     * @param autoRotate magnitude of rotational chassis vector
     */
    public void auto(float autoStrafe, float autoFwd, float autoRotate) {
        //spoof the joystick's input for auto and run
        gamepad.right_stick_x = autoRotate;
        gamepad.left_stick_x = autoStrafe;
        gamepad.left_stick_y = autoFwd;
        if (isThread) {
            if (!thread.isAlive()) {
                thread.start();
            }
        } else{
            run();
        }
    }

}