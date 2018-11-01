package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class Outake extends Mech{
    DcMotor raiseMotor;
    Servo pivot;
    EncoderThread raiseMotorEncoder;

    public static final double MAX_LINEAR_TRAVEL = 18;//TODO set these
    public static final double MAX_SERVO_POSITION = 180;
    public static final double HOME_SERVO_POSITION = 0;
    public static final double WHEEL_RADIUS = 1.75;

    public Outake(DcMotor raiseMotor, Servo pivot){
        this.raiseMotor = raiseMotor;
        this.pivot = pivot;

        raiseMotorEncoder = new EncoderThread(raiseMotor, 40);
        raiseMotorEncoder.setRadius(WHEEL_RADIUS);
    }

    public Outake(){}

    @Override
    public void init(HardwareMap hardwareMap){
        DcMotor slideMotor = hardwareMap.get(DcMotor.class, "Output Raise");
        Servo pivot = hardwareMap.get(Servo.class, "Pivot Output");

        this.raiseMotor = slideMotor;
        this.pivot = pivot;
    }

    @Override
    public void run(){
        if(gamepad.a){
            if(!isUp()) {
                raise();
            }else {
                dump();
            }
        }else{
            unDump();
            lower();
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

    public void dump(){
        pivot.setPosition(MAX_SERVO_POSITION);
    }

    public void unDump(){
        pivot.setPosition(HOME_SERVO_POSITION);
    }

    public void lower(){
        raiseMotorEncoder.runToPosLinear(1, 0);
    }

    public void raise(){
        raiseMotorEncoder.runToPosLinear(1, MAX_LINEAR_TRAVEL);
    }
}
