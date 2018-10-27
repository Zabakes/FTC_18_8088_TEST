package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


/*
wheels numbered
    1-------2
    |       |
    |       |
    4-------3
 */



public class SwerveDrive{
    public SwerveModule swerveModules[];
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

            double STR;
            double FWD;
            double RCW;

                 STR = gamepad1.left_stick_x;
                 FWD = gamepad1.left_stick_y;
                 RCW = gamepad1.right_stick_x;

            double rotateAngle = 45;

            if(navx != null) {
                rotateAngle += navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            }

            // write the speed and angles to all modules
            for(int i = 0; i < swerveModules.length; i++){

                rotateAngle += (360/swerveModules.length)*i;

                double xComponent = Math.sin(Math.toDegrees(rotateAngle))*RCW+STR;
                double yComponent = Math.cos(Math.toDegrees((rotateAngle)))*RCW+FWD;

                swerveModules[i].setWheelPosition(Math.toDegrees(Math.atan2(yComponent, xComponent)));
                swerveModules[i].Go((Math.sqrt(Math.pow(yComponent, 2)+Math.pow(xComponent, 2))/2));

            }
        }

        public void setAngle(double angle){
            this.angle = angle;
        }

        public void setSpeed(double speed){
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