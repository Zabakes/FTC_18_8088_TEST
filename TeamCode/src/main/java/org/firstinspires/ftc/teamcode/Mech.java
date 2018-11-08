package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 *an abstract definition of a mechanism including the ability to run on their own threads
 *
 *all mechs must implement run and init
 *
 *run is called by update and start which should be called in telop
 *
 *init should be called on init in both teleop and auto
 *
*/

public abstract class Mech implements Runnable {

    /**
     * set whether or not a given mech should run on it's own thread
     *
     * @param thread true if mech should run on it's own thread false if not
     */
    public void setThread(boolean thread) {//ability to turn threads on/off
        isThread = thread;
    }

    protected boolean isThread = false;//turn threads off by default
    Gamepad gamepad;//gamepad used for running the mechs
    Thread thread = new Thread(this);//the thread with this runnable (this run method)

    /**
     *
     * initialize a given mechanism this is because then mechanisms are completely independent
     *
     * @param hardwareMap hardwaremap from the phone
     */
    public abstract void init(HardwareMap hardwareMap);

    public abstract void auto();

    /**
     *
     * run the teleop functions of a mechanism on or off it's own thread. this keeps mechanisms isolated from opmodes so it's easy to take something off the robot. calls the run method of a mechanism if it runs on it's own thread this will not run two threads for a single mechanism
     *
     * @param gamepad gamepad from opmode to run based on
     */
    public void updateAndStart(Gamepad gamepad) {// update and start method for all mechs updates the gamepad and starts run
        this.gamepad = gamepad;//update gamepad
        if (isThread){//if this mech uses it's own thread.
            if (!thread.isAlive()) {//check to make sure no thread of this mech is running already.
                thread.start();//start the thread with this mech is a runnable.
            }
        } else {
            this.run();//otherwise just call the run method normally
        }
    }

}
