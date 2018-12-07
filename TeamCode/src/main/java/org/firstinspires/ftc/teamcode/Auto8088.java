package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.openftc.revextensions2.RevExtensions2;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


public class Auto8088 {
    private static VuforiaLocalizer vuforia;

    private static Intakearm intake = new Intakearm();
    private static Chassis chassis = new Chassis(16,16);//TODO set these
    private static Outake outake = new Outake();


    public static final Double BACKUP_DISTANCE = 1.0;//TODO set these
    public static final Double TO_GOLD_DEGREES = 5.0;
    public static Double TO_CRATER_DEGREES = 10.0;
    public static Double TO_CRATER_DISTANCE = 10.0;
    public static Double TO_DEPO_DISTANCE = 10.0;

    public static void init(HardwareMap h){
        RevExtensions2.init();
        intake.init(h);
        chassis.init(h);
        outake.init(h);
    }

    public static void runOpMode(boolean isNearCrater) {

        outake.unClimb();
        chassis.go(1, BACKUP_DISTANCE);
        outake.lower();

        float turnPower;
        double goDistance;

        switch (goldPosition()){
            case LEFT:
                turnPower = -1;
                goDistance = 5;
                TO_CRATER_DEGREES += TO_GOLD_DEGREES;
                break;
            case RIGHT:
                turnPower = 1;
                goDistance = 5;
                TO_CRATER_DEGREES -= TO_GOLD_DEGREES;
                break;
            default:
                turnPower = 0;
                goDistance = 3;
        }

        chassis.turn(turnPower, TO_GOLD_DEGREES);
        chassis.go(1, goDistance);

        if(isNearCrater){
            chassis.go(1,4);
        }else{
            chassis.turn(-turnPower, TO_GOLD_DEGREES*2);
            chassis.go(1,TO_DEPO_DISTANCE);
            intake.retract();
            chassis.turn(1,TO_CRATER_DEGREES);
            chassis.go(1,TO_CRATER_DISTANCE);
            intake.lower();
            intake.extend();
        }

    }


    private static goldPosition goldPosition(){

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
        */

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        String VUFORIA_KEY = "Abi6F+//////AAABmep7SaMFl0URvHiThF3uK9ZQnbe7CnIYsFsZ1TSTvxERrwrlSgvq34SM2VwisdIywRTkO2bAgRN4H75v0/jG7+YnAM2QQmXYj0DTYfjc6ix8oolw1vUs+/a/TV0yuTtlJH7cEnYpu3wfeJyvCpksHdQG2A8CH8atWp67CL5iLMb7WXRzW2cIk6BVNAyPyQ/HoRE5AVyhjL0JdjdMeCpuifoNqm0UoKF6XBhM3bgGM1tvcK9rjYlgTKBFlda2t3Jj+4fqnUYvr8UhEjxXNqjVv8wwipdMhZJtGGosGAuZ1UMFi7mz5EGYLuP5POg+iGfHC3lK/zYq4V1douihhi+gb7krUGDL51VFF0/AVkAJFzzp";

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(1); //tells VuforiaLocalizer to only store one frame at a time

        /*To access the image: you need to iterate through the images of the frame object:*/

        VuforiaLocalizer.CloseableFrame frame = null;

        try {
            frame = vuforia.getFrameQueue().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        goldPosition position = goldPosition.CENTER;

        if(frame != null) {

            int silverAvgX = 0;//sum of all x coordinates belonging to pixels with a red value above the threshold used for averaging
            int goldAvgX = 0;//sum of all x coordinates belonging to pixels with a blue value above the threshold used for averaging
            int silverNum = 0;
            int goldNum = 0;

            Bitmap pic = vuforia.convertFrameToBitmap(frame);


            //TODO set these
            int RedUpperthresholdG = 254;
            int RedLowerthresholdG = 254;
            int GreenUpperthresholdG = 254;
            int GreenLowerthresholdG = 254;
            int BlueUpperthresholdG = 254;
            int BlueLowerthresholdG = 254;

            int RedUpperthresholdB = 254;
            int RedLowerthresholdB = 254;
            int GreenUpperthresholdB = 254;
            int GreenLowerthresholdB = 254;
            int BlueUpperthresholdB = 254;
            int BlueLowerthresholdB = 254;

            for (int y = 0; y < pic.getHeight(); y++) {
                for (int x = 0; x < pic.getWidth(); x++) {

                    if (Color.red(pic.getPixel(x, y)) <= RedUpperthresholdG && Color.red(pic.getPixel(x, y)) >= RedLowerthresholdG
                            && Color.green(pic.getPixel(x, y)) <= GreenUpperthresholdG && Color.green(pic.getPixel(x, y)) >= GreenLowerthresholdG
                            && Color.blue(pic.getPixel(x, y)) <= BlueUpperthresholdG && Color.blue(pic.getPixel(x, y)) >= BlueLowerthresholdG
                            ) {
                        goldAvgX += x;
                        goldNum++;
                    }

                    if (Color.red(pic.getPixel(x, y)) <= RedUpperthresholdB && Color.red(pic.getPixel(x, y)) >= RedLowerthresholdB
                            && Color.green(pic.getPixel(x, y)) <= GreenUpperthresholdB && Color.green(pic.getPixel(x, y)) >= GreenLowerthresholdB
                            && Color.blue(pic.getPixel(x, y)) <= BlueUpperthresholdB && Color.blue(pic.getPixel(x, y)) >= BlueLowerthresholdB
                            ) {
                        silverAvgX += x;
                        silverNum++;
                    }
                }
            }



        int tolerance = 10;

        int numGoldPixReq = 100;
        int numSilverPixReq = 200;

        if(goldNum >= numGoldPixReq && silverNum >= numSilverPixReq) {

            if (goldAvgX + tolerance > silverAvgX) {
                position = goldPosition.RIGHT;
            } else if (goldAvgX - tolerance < silverAvgX) {
                position = goldPosition.LEFT;
            }
        }
        }
        return position;
        }


}


enum goldPosition{
    LEFT,RIGHT,CENTER
}