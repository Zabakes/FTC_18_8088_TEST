package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Autonomous(name="auto recod", group="Iterative Opmode")


public class AutoRecord extends OpMode {
    File Inputs;
    FileOutputStream outputStream;
    int i = 0;
    Activity a = new Activity();

    @Override
    public void init() {

        File directory = a.getFilesDir();

        while (true) {
            try {
                a.openFileInput("auto"+ i);
            }catch (FileNotFoundException e){
                break;
            }
            i++;
        }
        Inputs = new File(directory, "auto" + i);
    }

    @Override
    public void loop() {
        try {
        outputStream = a.openFileOutput("auto"+i, Context.MODE_PRIVATE);

        outputStream.write(("A" + gamepad1.a).getBytes());
        outputStream.write(("B" + gamepad1.b).getBytes());
        outputStream.write(("X" + gamepad1.x).getBytes());
        outputStream.write(("Y" + gamepad1.y).getBytes());

        outputStream.write(("RSX" + gamepad1.right_stick_x).getBytes());
        outputStream.write(("RSY" + gamepad1.right_stick_y).getBytes());

        outputStream.write(("LSX" + gamepad1.left_stick_x).getBytes());
        outputStream.write(("LSY" + gamepad1.left_stick_y).getBytes());

        outputStream.write(("LT" + gamepad1.left_trigger).getBytes());
        outputStream.write(("RT" + gamepad1.right_trigger).getBytes());

        outputStream.write(("LB" + gamepad1.left_bumper).getBytes());
        outputStream.write(("RB" + gamepad1.right_bumper).getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
