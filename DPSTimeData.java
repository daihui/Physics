/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightseconddataanalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import com.sun.scenario.effect.Offset;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author 戴辉
 */
public class DPSTimeData {

    public static void main(String[] args) throws IOException, DeviceException {
        //TimeDataTestMode();
        TimeData();
        //writeTest();
        // int[] randomSend= RandomReadTest();
//        int sendCode=decodeSend(111, 96, randomSend);
//        System.out.println(sendCode);

    }

    public static void TimeData() throws IOException, DeviceException {
        File pxiFileR = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-15\\receive\\20150115121232-R-真随机-APD1小-9_时间测量数据.dat");
        File caliFileR = null;
        TimeEventLoader pxiLoaderR = new PXI40PS1Loader(pxiFileR, caliFileR);
        TimeEventSegment pxiSegmentR = TimeEventDataManager.loadTimeEventSegment(pxiLoaderR);

        TimeEventList GPSListR = pxiSegmentR.getEventList(0);
        TimeEventList SyncListR = pxiSegmentR.getEventList(1);
        TimeEventList APD1ListR = pxiSegmentR.getEventList(2);
        TimeEventList APD2ListR = pxiSegmentR.getEventList(3);

        File pxiFileS = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-15\\send\\20150115121232-s-真随机数9_随机数_时间测量数据.dat");
        File caliFileS = null;
        TimeEventLoader pxiLoaderS = new PXI40PS1Loader(pxiFileS, caliFileS);
        TimeEventSegment pxiSegmentS = TimeEventDataManager.loadTimeEventSegment(pxiLoaderS);

        TimeEventList GPSListS = pxiSegmentS.getEventList(0);
        TimeEventList SyncListS = pxiSegmentS.getEventList(1);

        System.out.println("Send GPS: " + GPSListS.size() / 60 + "\t" + GPSListS.size());
        System.out.println("Send Sync: " + SyncListS.size() / 60 + "\t" + SyncListS.size());
        System.out.println("Receive GPS: " + GPSListR.size() / 60 + "\t" + GPSListR.size());
        System.out.println("Receive Sync: " + SyncListR.size() / 60 + "\t" + SyncListR.size());
        System.out.println("APD1: " + APD1ListR.size() / 60 + "\t" + APD1ListR.size());
        System.out.println("APD2: " + APD2ListR.size() / 60 + "\t" + APD2ListR.size());

        // writeTxt(GPSList,new String("GPS"));
//        writeTxt(SyncListR, "Receive Sync");
//        writeTxt(SyncListS, "Send Sync");
        APDwriteTxt(SyncListS, SyncListR, APD1ListR, APD2ListR, "APD 数据分析", 98589100, 98581850, 800);
        //APDwriteTxt(SyncList,APD2List,new String("APD2"),98575000);

//        for(int i=0;i<SyncList.size();i++){
//             System.out.println("Round: "+ i + "Sync Time: " + SyncList.get(i).getTime() + "\t" );
//             
//        }
//        for(int i=0;i<APD1List.size();i++){
//             System.out.println("APD1: " + (APD1List.get(i+1).getTime()-APD1List.get(i).getTime())/1000.0 + "\t" );
//        }
    }

    public static void writeTxt(TimeEventList list, String s) throws IOException {
        Date dt = new Date();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);
        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + s + " " + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        writename.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(s + "\r\n");
        for (int i = 0; i < list.size(); i++) {
            out.write(list.get(i).getTime() + "\r\n");//将时间数据写入TXT文件
            // System.out.println("GPS: " + GPSList.get(i) + "\t" );  
        }
        out.flush(); // 把缓存区内容压入文件  
        out.close(); // 最后记得关闭文件  
    }

    public static void APDwriteTxt(TimeEventList SyncListS, TimeEventList SyncListR, TimeEventList APD1_List, TimeEventList APD2_List, String s, int DelayTime1, int DelayTime2, int TimeGate) throws IOException {

        Date dt = new Date();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);

        int APD1_Index = 0, APD2_Index = 0;
        int APD1_Code1_count = 0, APD2_Code1_count = 0;
        int APD1_Code0_count = 0, APD2_Code0_count = 0;
        int APD1_PositionErrorCount = 0, APD2_PositionErrorCount = 0;
        int APD1_PulseCountError = 0, APD2_PulseCountError = 0;
        int CodeError = 0;
        int SyncIndexOffset = 0;
        int SyncRound = 0;

        File randomdata = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-15\\send\\20150115121232-s-真随机数9_随机数_随机数.dat");
        FileInputStream randomDataIn = new FileInputStream(randomdata);
        DataInputStream randomData = new DataInputStream(randomDataIn);

        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + s + " " + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        writename.createNewFile(); // 创建数据处理结果文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(s + "\r\n");

        File codewrite = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + "成码数据" + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        codewrite.createNewFile(); // 创建新文件  
        BufferedWriter code = new BufferedWriter(new FileWriter(codewrite));
        code.write("接收端成码 " + "\t" + "发射端编码 " + "\r\n");

        if (SyncListS.size() >= SyncListR.size()) {
            SyncRound = (SyncListR.size());
        } else {
            SyncRound = (SyncListS.size());
        }

        for (int i = 0; i < 7000; i++) {
            //大round循环

            long APD1_Time = APD1_List.get(APD1_Index).getTime();
            long APD2_Time = APD2_List.get(APD2_Index).getTime();
            long SyncTimeR = SyncListR.get(i).getTime();
            int Count1 = 0, Count2 = 0;
            int PulsePosition1 = 0, PulsePosition2 = 0;
            boolean Singal1 = false, Singal2 = false;
            int[] randomSend = new int[128];
            int SendOffset = 0;
            // out.write("Sync round "+i+"\r\n");
            if ((i + SyncIndexOffset) > SyncListS.size()) {
                break;
            }
            SendOffset = TimeSync(SyncListS, SyncListR, i + SyncIndexOffset, i);
            SyncIndexOffset += SendOffset;
            for (int index = 0; index <= SendOffset; index++) {
                randomSend = RandomRead(randomData, 0, 16);//读取此round的发射端随机数
            }
            while (APD1_Time < (SyncTimeR + DelayTime1)) {
                //找到此round 中APD1时间事件的第一个脉冲;
                APD1_Index++;
                if (APD1_Index >= APD1_List.size()) {
                    break;
                }
                APD1_Time = APD1_List.get(APD1_Index).getTime();
            };
            while (APD2_Time < (SyncTimeR + DelayTime2)) {
                // 找到此round 中APD2时间事件的第一个脉冲;;
                APD2_Index++;
                if (APD2_Index >= APD2_List.size()) {
                    break;
                }
                APD2_Time = APD2_List.get(APD2_Index).getTime();
            };
            if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                break;
            }
            // while(APDTime<(SyncTime+DelayTime+256000)){

            for (int j = 0; j < 128; j++) {
                //记录128个脉冲的时间事件位置
                APD1_Time = APD1_List.get(APD1_Index).getTime();
                APD2_Time = APD2_List.get(APD2_Index).getTime();
                // System.out.println(1);
                while (APD1_Time < (SyncTimeR + DelayTime1 + j * 2000)) {
                    // APD1位置标记;
                    if (APD1_Time > (SyncTimeR + DelayTime1 + j * 2000 - 1000 - 0.5 * TimeGate) && (APD1_Time < (SyncTimeR + DelayTime1 + j * 2000 - 1000 + 0.5 * TimeGate))) {
                        PulsePosition1 = j;
                        Count1++;
                    }
                    APD1_Index++;

                    if (APD1_Index >= APD1_List.size()) {
                        break;
                    }
                    APD1_Time = APD1_List.get(APD1_Index).getTime();

                };

                while (APD2_Time < (SyncTimeR + DelayTime2 + j * 2000)) {
                    // APD2位置标记;
                    if (APD2_Time > (SyncTimeR + DelayTime2 + j * 2000 - 1000 - 0.5 * TimeGate) && (APD2_Time < (SyncTimeR + DelayTime2 + j * 2000 - 1000 + 0.5 * TimeGate))) {
                        PulsePosition2 = j;
                        Count2++;
                    }
                    APD2_Index++;
                    if (APD2_Index >= APD2_List.size()) {
                        break;
                    }
                    APD2_Time = APD2_List.get(APD2_Index).getTime();
                };
                if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                    break;
                }
            }
            if ((Count1 == 1) && (PulsePosition1 >= 15)) {
                int sendCode = decodeSend(PulsePosition1, PulsePosition1 - 15, randomSend);
                Singal1 = true;
                Count1 = 0;
                if (sendCode == 1) {
                    APD1_Code1_count++;
                } else {
                    APD1_Code0_count++;
                }
                out.write("1 Round: " + i + "\t" + "Time: " + APD1_Time + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "SendCode: " + sendCode + "\r\n");
                //将时间数据与位置记录写入TXT文件
            } else if (Count1 == 1 && PulsePosition1 < 15) {
                Count1 = 0;
                APD1_PositionErrorCount++;
                out.write("1 Round: " + i + "\t" + "Time: " + APD1_Time + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
            };
            if (Count1 > 1) {
                APD1_PulseCountError++;
                Count1 = 0;
            };

            if ((Count2 == 1) && (PulsePosition2 >= 15)) {
                Singal2 = true;
                Count2 = 0;
                int sendCode = decodeSend(PulsePosition2, PulsePosition2 - 15, randomSend);
                if (sendCode == 1) {
                    APD2_Code1_count++;
                } else {
                    APD2_Code0_count++;
                }
                out.write("2 Round: " + i + "\t" + "Time: " + APD2_Time + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "SendCode: " + sendCode + "\r\n");//将时间数据写入TXT文件
            } else if (Count2 == 1 && PulsePosition2 < 15) {
                Count2 = 0;
                APD2_PositionErrorCount++;
                out.write("2 Round: " + i + "\t" + "Time: " + APD2_Time + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
            };
            if (Count2 > 1) {
                APD2_PulseCountError++;
                Count2 = 0;
            };

            //if(APD1_Index>=APD1_List.size()|APD2_Index>=APD2_List.size()){break;}
            boolean ifCodeSucess = Singal1 ^ Singal2;
            if (ifCodeSucess) {
                out.write(" Round: " + i + " Code is " + ifCodeSucess + "\r\n");
                if (Singal1 == true) {
                    int sendCode = decodeSend(PulsePosition1, PulsePosition1 - 15, randomSend);
                    if (sendCode == 0) {
                        code.write("APD1: " + 0 + "\t" + sendCode + "\r\n");
                    } else {
                        CodeError++;
                        code.write("APD1: " + 0 + "\t" + sendCode + " ERROR CODE!" + "\r\n");
                    }
                } else {
                    int sendCode = decodeSend(PulsePosition2, PulsePosition2 - 15, randomSend);
                    if (sendCode == 1) {
                        code.write("APD2: " + 1 + "\t" + sendCode + "\r\n");
                    } else {
                        CodeError++;
                        code.write("APD2: " + 1 + "\t" + sendCode + " ERROR CODE!" + "\r\n");
                    }
                }
            }
            if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                break;
            }
        }
        out.write("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
        out.write("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
        //写入编码统计数据、脉冲位置误码、光子计数误码
        out.write("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2 + "\t" + "TimeGate: " + TimeGate);
        out.flush(); // 把缓存区内容压入文件  
        out.close(); // 关闭文件  

        code.write("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
        code.write("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
        //写入编码统计数据、脉冲位置误码、光子计数误码
        code.write("Total Code Count: " + (APD1_Code0_count + APD1_Code1_count + APD2_Code0_count + APD2_Code1_count) + " Error Code Count: " + CodeError + "\r\n");
        code.write("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2 + "\t" + "TimeGate: " + TimeGate);
        code.flush();
        code.close();

        System.out.println("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2 + "\t" + "TimeGate: " + TimeGate);
        System.out.println("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
        System.out.println("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
        System.out.println("Total Code Count: " + (APD1_Code0_count + APD1_Code1_count + APD2_Code0_count + APD2_Code1_count) + " Error Code Count: " + CodeError + "\r\n");
    }

    public static int[] RandomReadTest() throws FileNotFoundException, IOException {
        //提取发射端随机数，存到一个128个元素的整型数组当中
        Date dt = new Date();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);
        File randomdata = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-14\\send\\20150114155127-s-4_随机数.dat");
        File randomdatTXT = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + date + "sendRandomdata.txt");
        FileInputStream in = new FileInputStream(randomdata);
        DataInputStream dis = new DataInputStream(in);
        byte[] b = new byte[16];
        int[] randomList = new int[128];

        dis.read(b, 0, 16);
        for (int i = 0; i < 128; i++) {
            if (((b[(i / 8)] >>> (7 - (i % 8))) & 0x01) == 0x01) {
                randomList[i] = 0;
            } else {
                randomList[i] = 1;
            }
        }
        //由于FPGA实际输出随机数错误，需要将文件中数据每两bit交换
//                for(int i=0;i<64;i++){
//                    int exchangeTamp=0;
//                    exchangeTamp=randomList[2*i];
//                    randomList[2*i]=randomList[2*i+1];
//                    randomList[2*i+1]=exchangeTamp;
//                }
        randomdatTXT.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(randomdatTXT));
        for (int i = 0; i < 128; i++) {
            out.write(randomList[i] + "\t");
        }
        out.flush();
        out.close();
        return randomList;
    }

    public static int TimeSync(TimeEventList SyncListS, TimeEventList SyncListR, int indexS, int indexR) {
        int indexSend = indexS;
        long STime = SyncListS.get(indexSend).getTime();
        long RTime = SyncListR.get(indexR).getTime();
        int SendOffset = 0;
        //long DTime=0;
        while (Math.abs(STime - RTime) > 300000) {
            if((STime-RTime)>50000000){
                SendOffset=-1;
                // System.out.println("Receive Sync fail! " +  "\t" + "Receive Sync Time: " + RTime);
                 break;
            }
            indexSend++;
            SendOffset++;
            if (indexSend > SyncListS.size()) {
                break;
            }
            STime = SyncListS.get(indexSend).getTime();
           // System.out.println("Send Sync Time: " + STime + "\t" + "Receive Sync Time: " + RTime);
        }
        return SendOffset;
    }

    public static int[] RandomRead(DataInputStream dis, int Offset, int len) throws FileNotFoundException, IOException {
        //提取发射端随机数，存到一个128个元素的整型数组当中

        byte[] b = new byte[len];
        int[] randomList = new int[128];

        dis.read(b, Offset, len);
        for (int i = 0; i < 128; i++) {
            if (((b[(i / 8)] >>> (7 - (i % 8))) & 0x01) == 0x01) {
                randomList[i] = 0;
            } else {
                randomList[i] = 1;
            }
        }
        //由于FPGA实际输出随机数错误，需要将文件中数据每两bit交换
//                for(int i=0;i<64;i++){
//                    int exchangeTamp=0;
//                    exchangeTamp=randomList[2*i];
//                    randomList[2*i]=randomList[2*i+1];
//                    randomList[2*i+1]=exchangeTamp;
//                }

        return randomList;
    }

    public static int decodeSend(int lightPulsePosition, int lightDelay, int[] randamSend) {
        //根据光脉冲位置和发射随机数计算发射端的成码
        int sendCode = randamSend[lightPulsePosition] ^ randamSend[lightDelay];
        return sendCode;
    }

    public static void TimeDataTestMode() throws IOException, DeviceException {
        File pxiFile = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-14\\receive\\20150114155127-R-固定随机数-APD2小-4_时间测量数据.dat");
        File caliFile = null;
        TimeEventLoader pxiLoader = new PXI40PS1Loader(pxiFile, caliFile);
        TimeEventSegment pxiSegment = TimeEventDataManager.loadTimeEventSegment(pxiLoader);

        TimeEventList GPSList = pxiSegment.getEventList(0);
        TimeEventList SyncList = pxiSegment.getEventList(1);
        TimeEventList APD1List = pxiSegment.getEventList(2);
        TimeEventList APD2List = pxiSegment.getEventList(3);
        System.out.println("GPS: " + GPSList.size() / 60 + "\t" + GPSList.size());
        System.out.println("Sync: " + SyncList.size() / 60 + "\t" + SyncList.size());
        System.out.println("APD1: " + APD1List.size() / 60 + "\t" + APD1List.size());
        System.out.println("APD2: " + APD2List.size() / 60 + "\t" + APD2List.size());

        // writeTxt(GPSList,new String("GPS"));
        // writeTxt(SyncList,new String("Sync"));
        // writeTxt(APD1List,new String("APD1"));
        APDwriteTxtTestMode(SyncList, APD1List, APD2List, "APD 数据分析", 98581200, 98574300, 800);
        //APDwriteTxt(SyncList,APD2List,new String("APD2"),98575000);
        // TimeScan(SyncList, APD1List, APD2List, 98579000, 9872500, 200,1000);

    }

    public static void TimeScan(TimeEventList SyncList, TimeEventList APD1List, TimeEventList APD2List, int DelayTime1, int DelayTime2, int ScanStep, int TimeGate) throws IOException {
        int CodeCount = 0;
        int MaxrCount1 = 0, MaxCount2 = 0;
        int index1 = 0, index2 = 0;
        for (int i = 0; i < 20; i++) {
            System.out.println("APD2 Time Scan: " + i);
            CodeCount = APDwriteTxtTestMode(SyncList, APD1List, APD2List, "APD 数据分析", DelayTime1, DelayTime2 + i * ScanStep, TimeGate);
            if (MaxCount2 < CodeCount) {
                MaxCount2 = CodeCount;
                index2 = i;
            }
        }

        for (int i = 0; i < 20; i++) {
            System.out.println("APD1 Time Scan: " + i);
            CodeCount = APDwriteTxtTestMode(SyncList, APD1List, APD2List, "APD 数据分析", DelayTime1 + i * ScanStep, DelayTime2, TimeGate);
            if (MaxrCount1 < CodeCount) {
                MaxrCount1 = CodeCount;
                index1 = i;

            }
        }

        System.out.println("APD1 DelayTime: " + (DelayTime1 + index1 * ScanStep) + "\t" + "CodeCount: " + MaxrCount1 + "\t" + "APD2 DelayTime: " + (DelayTime2 + index2 * ScanStep) + "\t" + "CodeCount: " + MaxCount2);

    }

    public static int APDwriteTxtTestMode(TimeEventList SyncList, TimeEventList APD1_List, TimeEventList APD2_List, String s, int DelayTime1, int DelayTime2, int TimeGate) throws IOException {

        Date dt = new Date();
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);

        int APD1_Index = 0, APD2_Index = 0;
        int APD1_Code1_count = 0, APD2_Code1_count = 0;
        int APD1_Code0_count = 0, APD2_Code0_count = 0;
        int APD1_PositionErrorCount = 0, APD2_PositionErrorCount = 0;
        int APD1_PulseCountError = 0, APD2_PulseCountError = 0;
        int CodeError = 0;

        File randomdata = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-14\\send\\20150114155127-s-4_随机数.dat");
        FileInputStream randomDataIn = new FileInputStream(randomdata);
        DataInputStream randomData = new DataInputStream(randomDataIn);

//        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + s + " " + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
//        writename.createNewFile(); // 创建数据处理结果文件  
//        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//        out.write(s + "\r\n");
//        File codewrite = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + "成码数据" + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
//        codewrite.createNewFile(); // 创建新文件  
//        BufferedWriter code = new BufferedWriter(new FileWriter(codewrite));
//        code.write("接收端成码 " + "\t" + "发射端编码 " + "\r\n");
        for (int i = 0; i < SyncList.size(); i++) {
            //大round循环
            int[] randomSend = RandomRead(randomData, 0, 16);//读取此round的发射端随机数
            long APD1_Time = APD1_List.get(APD1_Index).getTime();
            long APD2_Time = APD2_List.get(APD2_Index).getTime();
            long SyncTime = SyncList.get(i).getTime();
            int Count1 = 0, Count2 = 0;
            int PulsePosition1 = 0, PulsePosition2 = 0;
            boolean Singal1 = false, Singal2 = false;
            // out.write("Sync round "+i+"\r\n");
            while (APD1_Time < (SyncTime + DelayTime1)) {
                //找到此round 中APD1时间事件的第一个脉冲;
                APD1_Index++;
                if (APD1_Index >= APD1_List.size()) {
                    break;
                }
                APD1_Time = APD1_List.get(APD1_Index).getTime();
            };
            while (APD2_Time < (SyncTime + DelayTime2)) {
                // 找到此round 中APD2时间事件的第一个脉冲;;
                APD2_Index++;
                if (APD2_Index >= APD2_List.size()) {
                    break;
                }
                APD2_Time = APD2_List.get(APD2_Index).getTime();
            };
            if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                break;
            }
            // while(APDTime<(SyncTime+DelayTime+256000)){

            for (int j = 0; j < 128; j++) {
                //记录128个脉冲的时间事件位置
                APD1_Time = APD1_List.get(APD1_Index).getTime();
                APD2_Time = APD2_List.get(APD2_Index).getTime();
                // System.out.println(1);
                while (APD1_Time < (SyncTime + DelayTime1 + j * 2000)) {
                    // APD1位置标记;
                    if (APD1_Time > (SyncTime + DelayTime1 + j * 2000 - 1000 - 0.5 * TimeGate) && (APD1_Time < (SyncTime + DelayTime1 + j * 2000 - 1000 + 0.5 * TimeGate))) {
                        PulsePosition1 = j;
                        Count1++;
                    }
                    APD1_Index++;

                    if (APD1_Index >= APD1_List.size()) {
                        break;
                    }
                    APD1_Time = APD1_List.get(APD1_Index).getTime();

                };

                while (APD2_Time < (SyncTime + DelayTime2 + j * 2000)) {
                    // APD2位置标记;
                    if (APD2_Time > (SyncTime + DelayTime2 + j * 2000 - 1000 - 0.5 * TimeGate) && (APD2_Time < (SyncTime + DelayTime2 + j * 2000 - 1000 + 0.5 * TimeGate))) {
                        PulsePosition2 = j;
                        Count2++;
                    }
                    APD2_Index++;
                    if (APD2_Index >= APD2_List.size()) {
                        break;
                    }
                    APD2_Time = APD2_List.get(APD2_Index).getTime();
                };
                if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                    break;
                }
            }
            if ((Count1 == 1) && (PulsePosition1 >= 15)) {
                int sendCode = decodeSend(PulsePosition1, PulsePosition1 - 15, randomSend);
                Singal1 = true;
                Count1 = 0;
                if (sendCode == 1) {
                    APD1_Code1_count++;
                } else {
                    APD1_Code0_count++;
                }
//                out.write("1 Round: " + i + "\t" + "Time: " + APD1_Time + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "SendCode: " + sendCode + "\r\n");
//                //将时间数据与位置记录写入TXT文件
            } else if (Count1 == 1 && PulsePosition1 < 15) {
                Count1 = 0;
                APD1_PositionErrorCount++;
//                out.write("1 Round: " + i + "\t" + "Time: " + APD1_Time + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
            };
            if (Count1 > 1) {
                APD1_PulseCountError++;
                Count1 = 0;
            };

            if ((Count2 == 1) && (PulsePosition2 >= 15)) {
                Singal2 = true;
                Count2 = 0;
                int sendCode = decodeSend(PulsePosition2, PulsePosition2 - 15, randomSend);
                if (sendCode == 1) {
                    APD2_Code1_count++;
                } else {
                    APD2_Code0_count++;
                }
//                out.write("2 Round: " + i + "\t" + "Time: " + APD2_Time + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "SendCode: " + sendCode + "\r\n");//将时间数据写入TXT文件
            } else if (Count2 == 1 && PulsePosition2 < 15) {
                Count2 = 0;
                APD2_PositionErrorCount++;
//                out.write("2 Round: " + i + "\t" + "Time: " + APD2_Time + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
            };
            if (Count2 > 1) {
                APD2_PulseCountError++;
                Count2 = 0;
            };

            //if(APD1_Index>=APD1_List.size()|APD2_Index>=APD2_List.size()){break;}
            boolean ifCodeSucess = Singal1 ^ Singal2;
            if (ifCodeSucess) {
//                out.write(" Round: " + i + " Code is " + ifCodeSucess + "\r\n");
                if (Singal1 == true) {
                    int sendCode = decodeSend(PulsePosition1, PulsePosition1 - 15, randomSend);
                    if (sendCode == 0) {
//                        code.write("APD1: " + 0 + "\t" + sendCode + "\r\n");
                    } else {
                        CodeError++;
//                        code.write("APD1: " + 0 + "\t" + sendCode + " ERROR CODE!" + "\r\n");
                    }
                } else {
                    int sendCode = decodeSend(PulsePosition2, PulsePosition2 - 15, randomSend);
                    if (sendCode == 1) {
//                        code.write("APD2: " + 1 + "\t" + sendCode + "\r\n");
                    } else {
                        CodeError++;
//                        code.write("APD2: " + 1 + "\t" + sendCode + " ERROR CODE!" + "\r\n");
                    }
                }
            }
            if (APD1_Index >= APD1_List.size() | APD2_Index >= APD2_List.size()) {
                break;
            }
        }
//        out.write("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
//        out.write("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
//        //写入编码统计数据、脉冲位置误码、光子计数误码
//        out.write("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2+"\t"+"TimeGate: "+TimeGate);
//        out.flush(); // 把缓存区内容压入文件  
//        out.close(); // 关闭文件  
//        
//        code.write("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
//        code.write("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
//        //写入编码统计数据、脉冲位置误码、光子计数误码
//        code.write("Total Code Count: "+(APD1_Code0_count+APD1_Code1_count+APD2_Code0_count+APD2_Code1_count)+" Error Code Count: "+CodeError+"\r\n");
//        code.write("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2+"\t"+"TimeGate: "+TimeGate);
//        code.flush();
//        code.close();
//        
        System.out.println("APD1 DelayTime: " + DelayTime1 + "\t" + "APD2 DelayTime: " + DelayTime2 + "\t" + "TimeGate: " + TimeGate);
        System.out.println("APD1: Code 1 count:" + APD1_Code1_count + "\t" + "Code 0 count:" + APD1_Code0_count + "\t" + "Position Error: " + APD1_PositionErrorCount + "\t" + "Count Error: " + APD1_PulseCountError + "\r\n");
        System.out.println("APD2: Code 1 count:" + APD2_Code1_count + "\t" + "Code 0 count:" + APD2_Code0_count + "\t" + "Position Error: " + APD2_PositionErrorCount + "\t" + "Count Error: " + APD2_PulseCountError + "\r\n");
        System.out.println("Total Code Count: " + (APD1_Code0_count + APD1_Code1_count + APD2_Code0_count + APD2_Code1_count) + " Error Code Count: " + CodeError + "\r\n");
        return (APD1_Code0_count + APD2_Code1_count);
    }

}
