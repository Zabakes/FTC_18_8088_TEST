package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class Outake extends MechThread{
    DcMotor raiseMotor;
    Servo piviot;
    EncoderThread raiseMotorEncoder;

    public static final double MAX_LINEAR_TRAVEL = 5000;//TODO set these
    public static final double MAX_SERVO_POSITION = 180;
    public static final double HOME_SERVO_POSITION = 0;
    public static final double WHEEL_RADIUS = 1.75;

    public Outake(DcMotor raiseMotor, Servo piviot){
        this.raiseMotor = raiseMotor;
        this.piviot = piviot;

        raiseMotorEncoder = new EncoderThread(raiseMotor);
        raiseMotorEncoder.setRadius(WHEEL_RADIUS);

    }

    public Outake(){}

    @Override
    public void init(HardwareMap hardwareMap){
        DcMotor slideMotor = hardwareMap.get(DcMotor.class, "Output Raise");
        Servo piviot = hardwareMap.get(Servo.class, "Pivot Output");

        this.raiseMotor = slideMotor;
        this.piviot = piviot;
    }

    @Override
    public void run(){
        if(gamepad.a){
            if(!isUp()) {
                raiseMotorEncoder.runToPosLinear(1, MAX_LINEAR_TRAVEL);
            }else {
                piviot.setPosition(MAX_SERVO_POSITION);
            }
        }else{
            raiseMotorEncoder.runToPosLinear(1, 0);
            piviot.setPosition(HOME_SERVO_POSITION);
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
