package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

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

public class SwerveDrive{

    public SwerveModule[] swerveModules;
    private double angle;
    private double speed;
    private NavxMicroNavigationSensor navx = null;

    public SwerveDrive(SwerveModule[] swerveModules) {
        this.swerveModules = swerveModules;
    }

    public SwerveDrive(SwerveModule[] swerveModules, NavxMicroNavigationSensor navx) {
        this.swerveModules = swerveModules;
        this.navx = navx;
    }

    public void run(Gamepad gamepad1){

            double STR = gamepad1.left_stick_x;
            double FWD = gamepad1.left_stick_y;
            double RCW = gamepad1.right_stick_x;

            double firstModuleAngle = 45;
            double joystickMagnitude = Math.sqrt(Math.pow(STR,2)+Math.pow(FWD , 2));
            double joystickAngle = Math.atan2(FWD, STR);
            double navxOffset = 0;

            if(navx != null) {
                navxOffset += navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
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
            Gamepad autoPlaceHolder = new Gamepad();
            autoPlaceHolder.right_stick_x = autoRotate;
            autoPlaceHolder.left_stick_x = autoStrafe;
            autoPlaceHolder.left_stick_y = autoFwd;
            run(autoPlaceHolder);
        }

    }