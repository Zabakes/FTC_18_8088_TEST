package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import static org.firstinspires.ftc.teamcode.Teleop8088.BACKLEFT;
import static org.firstinspires.ftc.teamcode.Teleop8088.BACKRIGHT;
import static org.firstinspires.ftc.teamcode.Teleop8088.FRONTLEFT;
import static org.firstinspires.ftc.teamcode.Teleop8088.FRONTRIGHT;

/*
wheels numbered
    1-------2
    |       |
    |       |
    4-------3
 */



public class SwerveDrive extends Thread {
    public SwerveModule swerveModules[];
    private double angle;
    private double speed;
    private Gamepad gamepad1;
    private boolean isDiverControlled = false;
    private boolean isAuto = false;
    private NavxMicroNavigationSensor navx = null;
    private double autoStrafe;
    private double autoFwd;
    private double autoRotate;



    public SwerveDrive(SwerveModule[] swerveModules, Gamepad gamepad1) {
        this.swerveModules = swerveModules;
        this.gamepad1 = gamepad1;
    }

    public SwerveDrive(SwerveModule[] swerveModules, Gamepad gamepad1, NavxMicroNavigationSensor navx) {
        this.swerveModules = swerveModules;
        this.gamepad1 = gamepad1;
        this.navx = navx;
    }

    public void Run() {
        while (true) {
            double STR = 0;
            double FWD = 0;
            double RCW = 0;
            double temp;
            if(isDiverControlled) {
                 STR = gamepad1.left_stick_x;
                 FWD = -gamepad1.left_stick_y;
                 RCW = gamepad1.right_stick_x;

            }else{
                if(isAuto) {
                    STR = autoStrafe;
                    FWD = autoFwd;
                    RCW = autoRotate;
                }
            }

            double[] speed = new double[swerveModules.length];
            double[] angle = new double[swerveModules.length];


            if(navx != null) {
                temp = (FWD * Math.cos(navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle))
                        + (STR * Math.sin(navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle));
                STR = (FWD * Math.sin(navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle))
                        + (STR * Math.cos(navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle));
                FWD = temp;
            }


            double a = STR - RCW;
            double b = STR + RCW;
            double c = FWD - RCW;
            double d = FWD + RCW;

            speed[FRONTLEFT] = Math.sqrt ((b * b) + (c * c));
            speed[FRONTRIGHT] = Math.sqrt ((b * b) + (d * d));
            speed[BACKRIGHT] = Math.sqrt ((a * a) + (d * d));
            speed[BACKLEFT] = Math.sqrt ((a * a) + (c * c));

            angle[FRONTLEFT] = Math.atan2 (b, c) / Math.PI;
            angle[FRONTRIGHT] = Math.atan2 (b, d) / Math.PI;
            angle[BACKRIGHT] = Math.atan2 (a, d) / Math.PI;
            angle[BACKLEFT] = Math.atan2 (a, c) / Math.PI;

            // write the speed and angles to all 4 modules
            for(int i = 0; i < swerveModules.length; i++){
                swerveModules[i].Go(speed[i]);
                swerveModules[i].setWheelPosition(angle[i]*180);
            }
        }
    }

        public void setAngle(double angle){
        try{
          this.wait(10);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.angle = angle;
        this.notify();
        }

        public void setSpeed(double speed){
        try{
            this.wait(10);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.speed = speed;
        this.notify();
    }

        public void DriverControl(boolean isDiverControlled){
            try{
                this.wait(10);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.isDiverControlled = isDiverControlled;
            this.notify();
        }

        public double getAngle (){
            return angle;
        }

    public void setAutoStrafe(double autoStrafe) {
        try{
            this.wait(10);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.autoStrafe = autoStrafe;
    }

    public void setAutoFwd(double autoFwd) {
        try{
            this.wait(10);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.autoFwd = autoFwd;
        this.notify();
    }

    public void setAutoRotate(double autoRotate) {
        try{
            this.wait(10);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.autoRotate = autoRotate;
        this.notify();
    }
    }