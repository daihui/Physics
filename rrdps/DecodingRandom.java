package com.hwaipy.rrdps;

/**
 *
 * @author Hwaipy
 */
public class DecodingRandom {

    private  int delay1;
    private  int delay2;
    private  int APDFlag;

    public DecodingRandom(int delay1, int delay2) {
        this.delay1 = delay1;
        this.delay2 = delay2;
    }
    
    public DecodingRandom(int delay1, int delay2,int APDFlag) {
        this.delay1 = delay1;
        this.delay2 = delay2;
        this.APDFlag = APDFlag;
    }

    public int getDelay1() {
        return delay1;
    }

    public int getDelay2() {
        return delay2;
    }
    
    public int getAPDFlag(){
        return APDFlag;
    }

    public int getDelay() {
//        if(delay2>60){
//           return delay2 + 16 - delay1;
//        } else{ return delay2 + 15 - delay1;}
         return delay2 + 15 - delay1;
    }
}
