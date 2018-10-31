package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

class Outake {
    DcMotor raiseMotor;
    Servo piviot;
    Gamepad gamepad;
    EncoderThread raiseMotorEncoder;

    public static final double MAX_LINEAR_TRAVEL = 5000;//TODO set these
    public static final double MAX_SERVO_POSITION = 180;
    public static final double WHEEL_RADIUS = 1.75;

    public Outake(DcMotor raiseMotor, Servo piviot){
        this.raiseMotor = raiseMotor;
        this.piviot = piviot;

        raiseMotorEncoder = new EncoderThread(raiseMotor);
        raiseMotorEncoder.setRadius(WHEEL_RADIUS);

    }

    public void run(Gamepad gamepad){
        if(gamepad.a){
            if(!isUp()) {
                raiseMotorEncoder.runToPosLinear(1, MAX_LINEAR_TRAVEL);
            }else {
                piviot.setPosition(MAX_SERVO_POSITION);
            }
        }
    }

    public boolean isUp(){
        if(raiseMotor.getCurrentPosition() > MAX_LINEAR_TRAVEL){
            return true;
        }else{
            return false;
        }
    }

    public double howHigh(){
        return raiseMotorEncoder.getLinearPos();
    }


}
