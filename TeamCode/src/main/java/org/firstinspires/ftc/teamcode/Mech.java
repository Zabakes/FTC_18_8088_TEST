package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

abstract class MechThread implements Runnable{

    Gamepad gamepad;
    Thread tempThread = null;

    public abstract void init(HardwareMap hardwareMap);

    public void updateAndStart(Gamepad gamepad){
        this.gamepad = gamepad;
        if(!tempThread.isAlive() || tempThread == null) {
            tempThread = new Thread(this);
            tempThread.start();
        }
    }

}
