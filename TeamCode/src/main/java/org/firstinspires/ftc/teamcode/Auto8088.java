package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcam;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name = "auto8080TEST", group = "Iterative Opmode")
public class Auto8088 extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();
    private Intakearm intake = new Intakearm();
    private Chassis chassis = new Chassis();
    private Outake outake = new Outake();
    private VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {

        intake.init(hardwareMap);
        chassis.init(hardwareMap);
        outake.init(hardwareMap);

        waitForStart();

        outake.unClimb();
        chassis.go(1, -5);
        //TODO something else get to the crater
        outake.lower();


    }

    private goldPosition goldPosition(){

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

        int silverAvgX = 0;//sum of all x coordinates belonging to pixels with a red value above the threshold used for averaging
        int goldAvgX = 0;//sum of all x coordinates belonging to pixels with a blue value above the threshold used for averaging
        int silverNum = 0;
        int goldNum = 0;

        if(frame != null) {

            Bitmap pic = vuforia.convertFrameToBitmap(frame);

            int RedUpperthresholdG = 254;//threshold that determines if a pixel is red or blue
            int RedLowerthresholdG = 254;//threshold that determines if a pixel is red or blue
            int GreenUpperthresholdG = 254;//threshold that determines if a pixel is red or blue
            int GreenLowerthresholdG = 254;//threshold that determines if a pixel is red or blue
            int BlueUpperthresholdG = 254;//threshold that determines if a pixel is red or blue
            int BlueLowerthresholdG = 254;//threshold that determines if a pixel is red or blue

            int RedUpperthresholdB = 254;//threshold that determines if a pixel is red or blue
            int RedLowerthresholdB = 254;//threshold that determines if a pixel is red or blue
            int GreenUpperthresholdB = 254;//threshold that determines if a pixel is red or blue
            int GreenLowerthresholdB = 254;//threshold that determines if a pixel is red or blue
            int BlueUpperthresholdB = 254;//threshold that determines if a pixel is red or blue
            int BlueLowerthresholdB = 254;//threshold that determines if a pixel is red or blue

            for (int y = 0; y < pic.getHeight(); y++) {//iterating through every collum of pixels
                for (int x = 0; x < pic.getWidth(); x++) {//iterating through every row of pixels

                    if (Color.red(pic.getPixel(x, y)) <= RedUpperthresholdG && Color.red(pic.getPixel(x, y)) >= RedLowerthresholdG
                            && Color.green(pic.getPixel(x, y)) <= GreenUpperthresholdG && Color.green(pic.getPixel(x, y)) >= GreenLowerthresholdG
                            && Color.blue(pic.getPixel(x, y)) <= BlueUpperthresholdG && Color.blue(pic.getPixel(x, y)) >= BlueLowerthresholdG
                            ) {//testing if the pixel is red
                        goldAvgX += x;//adding the x value of the pixel that was previously determined to be red to the total x value of red pixels
                        goldNum++;//declaring that another red pixel was found
                    }

                    if (Color.red(pic.getPixel(x, y)) <= RedUpperthresholdB && Color.red(pic.getPixel(x, y)) >= RedLowerthresholdB
                            && Color.green(pic.getPixel(x, y)) <= GreenUpperthresholdB && Color.green(pic.getPixel(x, y)) >= GreenLowerthresholdB
                            && Color.blue(pic.getPixel(x, y)) <= BlueUpperthresholdB && Color.blue(pic.getPixel(x, y)) >= BlueLowerthresholdB
                            ) {//testing if the pixel is red
                        silverAvgX += x;//adding the x value of the pixel that was previously determined to be red to the total x value of red pixels
                        silverNum++;//declaring that another red pixel was found
                    }
                }
            }

        }

        int tolerance = 10;

        int numGoldPixReq = 100;
        int numSilverPixReq = 200;

        goldPosition position = goldPosition.CENTER;

        if(goldNum >= numGoldPixReq && silverNum >= numSilverPixReq) {

            if (goldAvgX + tolerance > silverAvgX) {
                position = goldPosition.RIGHT;
            } else if (goldAvgX - tolerance < silverAvgX) {
                position = goldPosition.LEFT;
            }
        }
        return position;
        }


}


enum goldPosition{
    LEFT,RIGHT,CENTER
}