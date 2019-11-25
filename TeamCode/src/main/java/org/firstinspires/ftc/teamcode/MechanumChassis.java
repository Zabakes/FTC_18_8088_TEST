package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class MechanumChassis extends Mech {

    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor[] motors;
    EncoderThread[] encoders;
    private BNO055IMU imu = null;

    private static double[] motorSpeeds = new double[4];

    private static final double PRot = 1;
    private static final double IRot = 1;
    private static final double DRot = 1;

    private static final double PPos = 1;
    private static final double IPos = 1;
    private static final double DPos = 1;

    @Override
    public void init(HardwareMap hardwareMap, Boolean opModeIsTeleOP) {
        //initialize the chassis

        frontLeft = hardwareMap.get(DcMotor.class, "Front Left Motor");
        frontRight = hardwareMap.get(DcMotor.class, "Front Right Motor");
        backRight = hardwareMap.get(DcMotor.class, "Back Right Motor");
        backLeft = hardwareMap.get(DcMotor.class, "Back Left Motor");

        motors = new DcMotor[]{frontLeft,frontRight,backLeft,backRight};
        encoders = new EncoderThread[motors.length];

        int i = 0;
        for (DcMotor m:motors) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            encoders[i] = new EncoderThread(m, 40);
            encoders[i].setRadius(3);
            i++;
        }

        if (hardwareMap.tryGet(BNO055IMU.class, "IMU") != null) {

            imu = hardwareMap.get(BNO055IMU.class, "IMU");

            if (imu.isGyroCalibrated() != true) {

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

    }

    @Override
    public String name() {
        return "Chassis";
    }

    @Override
    public void run() {
        runMotors();
    }

    private void runMotors(double[] speeds) {
        for (int i = 0; i < 4; i++) {
            motors[i].setPower(speeds[i]);
        }
    }

    private void runMotors() {
        for (int i = 0; i < 4; i++) {
            motors[i].setPower(motorSpeeds[i]);
        }
    }

    protected double[] upDateMotorSpeeds(){

        double speed          = Math.hypot(gamepad.left_stick_x, gamepad.left_stick_y);
        double angle = Math.atan2(gamepad.left_stick_y, gamepad.left_stick_x) - Math.PI / 4;
        double rotateSpeed     =  gamepad.right_stick_x;

        return upDateMotorSpeeds(speed, angle, rotateSpeed);
    }

    public double[] upDateMotorSpeeds(double speed,double angle, double rotateSpeed ){

        motorSpeeds [0] = speed * Math.cos(angle) + rotateSpeed;
        motorSpeeds [1] = speed * Math.sin(angle) - rotateSpeed;
        motorSpeeds [2] = speed * Math.sin(angle) + rotateSpeed;
        motorSpeeds [3] = speed * Math.cos(angle) - rotateSpeed;

        return motorSpeeds;
    }

    public double[] upDateMotorSpeeds(double rotateSpeed ){

        motorSpeeds [0] += rotateSpeed;
        motorSpeeds [1] += rotateSpeed;
        motorSpeeds [2] += rotateSpeed;
        motorSpeeds [3] += rotateSpeed;

        return motorSpeeds;
    }

    public boolean setOrientation(Orientation target, double toleranceRot, double tolerancePos, int timeOut){
        return setOrientationAndPosition(target, toleranceRot, imu.getPosition(), tolerancePos, timeOut);
    }

    public boolean setOrientationAndPosition(Orientation target, double toleranceRot, Position targetPosition, double tolerancePos,  int timeOut){

        double startTime = System.currentTimeMillis();
        double errorRot = target.thirdAngle - getOrientation().thirdAngle;
        double errorPos =  Math.pow(targetPosition.x-imu.getPosition().x,2)+Math.pow(targetPosition.z-imu.getPosition().z,2);
        double sumErrorRot = 0;
        double sumErrorPos = 0;
        
        do {

            double deltaErrorRot = errorRot - target.thirdAngle - getOrientation().thirdAngle;
            errorRot = target.thirdAngle - getOrientation().thirdAngle;
            sumErrorRot += errorRot;
            
            double newErrorPos = Math.pow(targetPosition.x-imu.getPosition().x,2)+Math.pow(targetPosition.z-imu.getPosition().z,2);
            double deltaErrorPos = errorPos - newErrorPos;
            errorPos = newErrorPos;
            sumErrorPos += errorPos;

            double movementAngle = Math.atan2(targetPosition.x-imu.getPosition().x, targetPosition.y-imu.getPosition().y);
            double movementSpeed = PPos*errorPos+IPos*sumErrorPos+DPos*deltaErrorPos;
            
            upDateMotorSpeeds(movementSpeed,movementAngle, PRot*errorRot+IRot*sumErrorRot+DRot*deltaErrorRot);
            
            
        }while (Math.abs(errorRot) > toleranceRot && Math.abs(errorPos) > tolerancePos && System.currentTimeMillis() - startTime < timeOut);

        return true;
    }

    private boolean setTargetPosition(Position targetPosition, double tolerancePos,double toleranceRot, int timeOut) {
        return setOrientationAndPosition(imu.getAngularOrientation(), toleranceRot, targetPosition, tolerancePos,timeOut);
    }

    public Orientation getOrientation(){
        return imu.getAngularOrientation();
    }

    public Velocity getVelocity(){
        return imu.getVelocity();
    }
}
