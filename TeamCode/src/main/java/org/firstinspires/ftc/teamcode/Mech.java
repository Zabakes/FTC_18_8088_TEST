package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

abstract class Mech implements Runnable{

    public void setThread(boolean thread) {
        isThread = thread;
    }

    protected boolean isThread = false;
    Gamepad gamepad;
    Thread thread = null;

    public abstract void init(HardwareMap hardwareMap);

    public void updateAndStart(Gamepad gamepad){
        this.gamepad = gamepad;
        if(isThread) {
            if (!thread.isAlive() || thread == null) {
                thread = new Thread(this);
                thread.start();
            }
        }else{
            this.run();
        }
    }

}
