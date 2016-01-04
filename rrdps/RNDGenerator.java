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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Random;

/**
 *
 * @author 戴辉
 */
public class RNDGenerator {
    public static void main(String[] args) throws IOException {
        RNDGenerator rNDGenerator = new RNDGenerator();
        rNDGenerator.RNDSeedGenerate(128,100);
        File rndseed = new File("G:\\DPS数据处理\\RNDGenerator\\128bit-100NumberSeed.txt");
        rNDGenerator.RNDRepeat(rndseed, 60);
    }

    public void RNDSeedGenerate(int bit, int Num) throws IOException {
        File rndwrite = new File("G:\\DPS数据处理\\RNDGenerator\\" + bit + "bit-" + Num + "NumberSeed" + ".txt "); // 相对路径，如果没有则要建立一个新的output。txt文件  
        rndwrite.createNewFile(); // 创建新文件  
        BufferedWriter rndBufferedWriter = new BufferedWriter(new FileWriter(rndwrite));
         Random random =new Random();
        for(int i=0;i<Num;i++){
            for (int j = 0; j < bit; j++) {
                 int r = random.nextInt(2);
                // System.out.println(r);
                rndBufferedWriter.write(r);
            }
            //rndBufferedWriter.write("\n");
        }
        rndBufferedWriter.flush();
        rndBufferedWriter.close();
        System.out.println("RNDSeed generate sucess!");
    }
    
    public void RNDRepeat(File RNDseed,int second) throws FileNotFoundException, IOException{
         File rndrepeatwrite = new File("G:\\DPS数据处理\\RNDGenerator\\" +second+ "secondRND" + ".txt "); // 相对路径，如果没有则要建立一个新的output。txt文件  
        rndrepeatwrite.createNewFile(); // 创建新文件  
        BufferedWriter rndrepeatBufferedWriter = new BufferedWriter(new FileWriter(rndrepeatwrite));
        int repeatNum=second*6600/100;
        for (int i = 0; i < repeatNum+200; i++) {
            InputStream rndseedinput = new BufferedInputStream(new FileInputStream(RNDseed), 200000);
            for (int j = 0; j < 128*100; j++) {
                int code =rndseedinput.read();
                rndrepeatBufferedWriter.write(code);
            }
        }
        rndrepeatBufferedWriter.flush();
        rndrepeatBufferedWriter.close();
         System.out.println("RND generate finish!");
    }
}
