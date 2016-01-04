/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hwaipy.rrdps;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author 戴辉
 */
public class WaveGenerator {
 private String parten1 = "1.00 \n" +
"0.98 \n" +
"0.96 \n" +
"0.94 \n" +
"0.90 \n" +
"0.86 \n" +
"0.84 \n" +
"0.78 \n" +
"0.76 \n" +
"0.74 \n" +
"0.72 \n" +
"0.70 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.68 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.66 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.64 \n" +
"0.62 \n" +
"0.62 \n" +
"0.62 \n" +
"0.62 \n" +
"0.62 \n" +
"0.62 \n" +
"0.62 \n";
        private String parten0 ="-1.00 \n" +
"-0.98 \n" +
"-0.96 \n" +
"-0.94 \n" +
"-0.90 \n" +
"-0.86 \n" +
"-0.82 \n" +
"-0.78 \n" +
"-0.76 \n" +
"-0.74 \n" +
"-0.72 \n" +
"-0.70 \n" +
"-0.68 \n" +
"-0.68 \n" +
"-0.68 \n" +
"-0.68 \n" +
"-0.68 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.64 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n" +
"-0.60 \n";
        private String partenflat1="0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.6\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.59\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n" +
"0.58\n";
        private String partenflat0="-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.58\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.57\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" +
"-0.56\n" ;
        
        public static void main(String[] args) throws IOException {
        WaveGenerator waveGenerator=new WaveGenerator();
        File RNDSeedFile=new File("G:\\DPS数据处理\\RNDGenerator\\128bit-100NumberSeed.txt");
        waveGenerator.WaveGenerate(RNDSeedFile, 100);
    }
    public void WaveGenerate(File RNDSeed,int Num) throws IOException {
        File waverndwrite = new File("G:\\DPS数据处理\\WaveGenerator\\" +"PM_Wave " +Num+ ".txt "); // 相对路径，如果没有则要建立一个新的output。txt文件  
        waverndwrite.createNewFile(); // 创建新文件  
        BufferedWriter waverndBufferedWriter = new BufferedWriter(new FileWriter(waverndwrite));
        InputStream rndinput = new BufferedInputStream(new FileInputStream(RNDSeed), 2000000);
        for(int i=0;i<Num-1;i++){
            for (int j = 0; j < 25000; j++) {
                waverndBufferedWriter.write("0\n");
            }
            for (int j = 0; j < 10; j++) {
                waverndBufferedWriter.write(parten1);
                waverndBufferedWriter.write(parten0);
            }
            int lastcode=0;
            for (int j = 0; j < 128; j++) {
                
                 int code= rndinput.read();
                 //System.out.println(code);
                 if(code==0){
                     if(lastcode==0){
                         waverndBufferedWriter.write(partenflat0);
                     }else{waverndBufferedWriter.write(parten0);}
                 }else if (code==1) {
                     if (lastcode==0) {
                         waverndBufferedWriter.write(parten1);
                     }else{waverndBufferedWriter.write(partenflat1);}
                }
                 lastcode=code;
                 
            }
             for (int j = 0; j < 2; j++) {
                waverndBufferedWriter.write(parten1);
                waverndBufferedWriter.write(parten0);
            }
             for (int j = 0; j < (2500000-25000-12*100-128*50); j++) {
                 waverndBufferedWriter.write("0\n");
            }
           
        }
        
         for (int j = 0; j < 25000; j++) {
                waverndBufferedWriter.write("0\n");
            }
            for (int j = 0; j < 10; j++) {
                waverndBufferedWriter.write(parten1);
                waverndBufferedWriter.write(parten0);
            }
            int lastcode=0;
            for (int j = 0; j < 128; j++) {
                
                 int code= rndinput.read();
                 //System.out.println(code);
                 if(code==0){
                     if(lastcode==0){
                         waverndBufferedWriter.write(partenflat0);
                     }else{waverndBufferedWriter.write(parten0);}
                 }else if (code==1) {
                     if (lastcode==0) {
                         waverndBufferedWriter.write(parten1);
                     }else{waverndBufferedWriter.write(partenflat1);}
                }
                 lastcode=code;
                 
            }
             for (int j = 0; j < 2; j++) {
                waverndBufferedWriter.write(parten1);
                waverndBufferedWriter.write(parten0);
            }
             for (int j = 0; j < 10000; j++) {
                 waverndBufferedWriter.write("0\n");
            }
        waverndBufferedWriter.flush();
        waverndBufferedWriter.close();
        
    }

    /**
     * @return the parten1
     */
    public String getParten1() {
        return parten1;
    }

    /**
     * @param parten1 the parten1 to set
     */
    public void setParten1(String parten1) {
        this.parten1 = parten1;
    }

    /**
     * @return the parten0
     */
    public String getParten0() {
        return parten0;
    }

    /**
     * @param parten0 the parten0 to set
     */
    public void setParten0(String parten0) {
        this.parten0 = parten0;
    }

    /**
     * @return the partenflat1
     */
    public String getPartenflat1() {
        return partenflat1;
    }

    /**
     * @param partenflat1 the partenflat1 to set
     */
    public void setPartenflat1(String partenflat1) {
        this.partenflat1 = partenflat1;
    }

    /**
     * @return the partenflat0
     */
    public String getPartenflat0() {
        return partenflat0;
    }

    /**
     * @param partenflat0 the partenflat0 to set
     */
    public void setPartenflat0(String partenflat0) {
        this.partenflat0 = partenflat0;
    }
}
