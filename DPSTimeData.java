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
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author 戴辉
 */
public class DPSTimeData {

    public static void main(String[] args) throws IOException, DeviceException {
        File SendTDFIle = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-25\\send\\20150125223254-S-随机-7_时间测量数据.dat");
        File ReceiveTDFile = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-29\\8W光子单路\\receive\\20150130001945_时间测量数据.dat");
        File SendRFile = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-25\\send\\20150125223254-S-随机-7_随机数.dat");
        File ReceiveRFile = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-25\\receive\\20150125234235-R-固定-PC126-稳相-APD2-11_随机数.dat");
        int APD1DelayTime = 98584300;
        int APD2DelayTime = 98577000;
        int TimeGate = 1000;
        int RoundStart = 0;
        int RoundEnd = 7500;
        int syncRound = 30000;

        //TimeDataTestMode();
        DPSTimeData process = new DPSTimeData();
        process.TimeData(SendTDFIle, ReceiveTDFile, SendRFile, ReceiveRFile, APD1DelayTime, APD2DelayTime, TimeGate, RoundStart, RoundEnd, syncRound);
        //writeTest();
        // int[] randomSend= RandomReadTest();
//        int sendCode=decodeSend(111, 96, randomSend);
//        System.out.println(sendCode);

    }

    public void TimeData(File SendTimeDataFile, File ReceiveTimeDataFile, File SendRandomFile, File ReceiveRandomFile, int APD1DelayTime, int APD2DelayTime, int Timegate, int RoundStart, int RoundEnd, int syncRound) throws IOException, DeviceException {
        File pxiFileR = ReceiveTimeDataFile;
        File caliFileR = null;
        TimeEventLoader pxiLoaderR = new PXI40PS1Loader(pxiFileR, caliFileR);
        TimeEventSegment pxiSegmentR = TimeEventDataManager.loadTimeEventSegment(pxiLoaderR);

        TimeEventList GPSListR = pxiSegmentR.getEventList(0);
        TimeEventList SyncListR = pxiSegmentR.getEventList(1);
        TimeEventList APD1ListR = pxiSegmentR.getEventList(2);
        TimeEventList APD2ListR = pxiSegmentR.getEventList(3);

        File pxiFileS = SendTimeDataFile;
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
//
        writeTxt(GPSListR, "Receive GPS");
        writeTxt(GPSListS, "Send GPS");
        writeTxt(SyncListR, "Receive Sync");
        writeTxt(SyncListS, "Send Sync");
        writeTxt(APD1ListR, "APD1");
        writeTxt(APD2ListR, "APD2");

        APDwriteTxt(SendRandomFile, ReceiveRandomFile, SyncListS, SyncListR, APD1ListR, APD2ListR, GPSListS, GPSListR, "APD 数据分析", APD1DelayTime, APD2DelayTime, Timegate, RoundStart, RoundEnd, syncRound);
        //APDwriteTxt(SyncList,APD2List,new String("APD2"),98575000);

//        for(int i=0;i<SyncList.size();i++){
//             System.out.println("Round: "+ i + "Sync Time: " + SyncList.get(i).getTime() + "\t" );
//             
//        }
//        for(int i=0;i<APD1List.size();i++){
//             System.out.println("APD1: " + (APD1List.get(i+1).getTime()-APD1List.get(i).getTime())/1000.0 + "\t" );
//        }
    }

    public void writeTxt(TimeEventList list, String s) throws IOException {
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

    public void APDwriteTxt(File SendRandomFile, File ReceiveRandomFile, TimeEventList SyncListS, TimeEventList SyncListR, TimeEventList APD1_List, TimeEventList APD2_List, TimeEventList GPSListS, TimeEventList GPSListR, String s, int DelayTime1, int DelayTime2, int TimeGate, int RoundStart, int RoundEnd, int syncRound) throws IOException {

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
        int GPSRound = 0;
        int SyncRound = 0;
        int[] staCountAPD1 = new int[128];
        int[] staCountAPD2 = new int[128];
        Arrays.fill(staCountAPD1, 0);
        Arrays.fill(staCountAPD2, 0);

        File sendrandomdata = SendRandomFile;
        FileInputStream sendrandomDataIn = new FileInputStream(sendrandomdata);
        DataInputStream SendRandomData = new DataInputStream(sendrandomDataIn);

        File receiverandomdata = ReceiveRandomFile;
        FileInputStream receiverandomDataIn = new FileInputStream(receiverandomdata);
        DataInputStream ReceiveRandomData = new DataInputStream(receiverandomDataIn);

        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + s + " " + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        writename.createNewFile(); // 创建数据处理结果文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(s + "\r\n");

        File codewrite = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + "成码数据" + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        codewrite.createNewFile(); // 创建新文件  
        BufferedWriter code = new BufferedWriter(new FileWriter(codewrite));
        code.write("接收端成码 " + "\t" + "发射端编码 " + "\r\n");

        File stacountFile = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + "统计" + date + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        stacountFile.createNewFile(); // 创建数据处理结果文件  
        BufferedWriter staCountOut = new BufferedWriter(new FileWriter(stacountFile));

        if (GPSListS.size() <= GPSListR.size()) {
            GPSRound = GPSListS.size();
        } else {
            GPSRound = GPSListR.size();
        }
//        if (GPSStartTime + ProcessTime - 1 > GPSRound) {
//            System.err.println("ERROR: Out of GPSRound, ProcessTime is too large！");
//        }

        long[] GPSTimeDelay = new long[GPSRound];      //GPS秒脉冲相对延时
        for (int i = 0; i < GPSRound; i++) {
            long GPS_RTime = GPSListR.get(i).getTime();
            long GPS_STime = GPSListS.get(i).getTime();
            GPSTimeDelay[i] = GPS_RTime - GPS_STime;
            System.out.println("GPSTimeDelay: " + GPSTimeDelay[i]);
        }

        if (SyncListS.size() <= SyncListR.size()) {
            SyncRound = SyncListS.size() - 4000;
        } else {
            SyncRound = SyncListR.size() - 4000;
        }
        System.out.println("SyncRound - 4000 = " + SyncRound);
        SyncRound = syncRound;
        for (int i = RoundStart; i < RoundEnd; i++) {

            long APD1_Time = APD1_List.get(APD1_Index).getTime();
            long APD2_Time = APD2_List.get(APD2_Index).getTime();
            long SyncTimeR = SyncListR.get(i).getTime();
            int Count1 = 0, Count2 = 0;
            int PulsePosition1 = 0, PulsePosition2 = 0;
            boolean Singal1 = false, Singal2 = false;
            int[] SendRandom = new int[128];
            int SendOffset = 0;
            int DelayPulse[] = new int[2];
            int GPS_RIndex = 0;
//            int GPS_SIndex=1;
            long GPSDelayTime = 0;

            if ((i + SyncIndexOffset) >= SyncListS.size()) {
                break;
            }
//            long SyncTimeS=SyncListS.get(i+SyncIndexOffset).getTime();
//            GPS_SIndex=(int) (SyncTimeS/1000000000);
//            GPS_SIndex=(GPS_SIndex/1000);
            GPS_RIndex = (int) (SyncTimeR / 1000000000);
            GPS_RIndex = (GPS_RIndex / 1000);

            GPSDelayTime = GPSTimeDelay[GPS_RIndex - 1];

            SendOffset = TimeSync(SyncListS, SyncListR, i + SyncIndexOffset, i, GPSDelayTime);
            if (SendOffset == -1) {//如果为-1，表示发射端同步信号时间大于接收端，此时发射端应不动，而接收端信号往后寻找
                //注意！！！ 发射端时间数据转换有误！
                SyncIndexOffset += SendOffset;
                DelayPulse = ReceiveRandomRead(ReceiveRandomData, 0, 1);
                continue;
            } else if (SendOffset >= 0) {
                SyncIndexOffset += SendOffset;
                   DelayPulse = ReceiveRandomRead(ReceiveRandomData, 0, 1);//读取接收端Pocells随机数
                   for (int index = 0; index <= SendOffset; index++) {
                SendRandom = SendRandomRead(SendRandomData, 0, 16);//读取此round的发射端随机数
            }
         
            }
            
//            DelayPulse[0]=0;
//            DelayPulse[1]=0;

            while (APD1_Time < (DelayTime1 + SyncTimeR+DelayPulse[0] * 2000)) {
                //找到此round 中APD1时间事件的第一个脉冲;
                APD1_Index++;
                if (APD1_Index >= APD1_List.size()) {
                    break;
                }
                APD1_Time = APD1_List.get(APD1_Index).getTime();
            };
            while (APD2_Time < (SyncTimeR + DelayTime2 + DelayPulse[0] * 2000)) {
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
                while (APD1_Time < (SyncTimeR + DelayTime1 + (j + 1) * 2000 + DelayPulse[0] * 2000)) {
                    // APD1位置标记;
                    staCountAPD1[j]++;
                    if (APD1_Time > (SyncTimeR + DelayTime1 + (j + 1) * 2000 + DelayPulse[0] * 2000 - 1000 - 0.5 * TimeGate)
                            && (APD1_Time < (SyncTimeR + DelayTime1 + (j + 1) * 2000 + DelayPulse[0] * 2000 - 1000 + 0.5 * TimeGate))) {
                        PulsePosition1 = j;
                        Count1++;
                    }
                    APD1_Index++;

                    if (APD1_Index >= APD1_List.size()) {
                        break;
                    }
                    APD1_Time = APD1_List.get(APD1_Index).getTime();

                };

                while (APD2_Time < (SyncTimeR + DelayTime2 + (j + 1) * 2000 + DelayPulse[0] * 2000)) {
                    // APD2位置标记;
                    staCountAPD2[j]++;
                    if (APD2_Time > (SyncTimeR + DelayTime2 + (j + 1) * 2000 + DelayPulse[0] * 2000 - 1000 - 0.5 * TimeGate)
                            && (APD2_Time < (SyncTimeR + DelayTime2 + (j + 1) * 2000 + DelayPulse[0] * 2000 - 1000 + 0.5 * TimeGate))) {
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
            if ((Count1 == 1) && (PulsePosition1 >= (DelayPulse[1] + 15 - DelayPulse[0]))) {
                int sendCode = decodeSend(PulsePosition1, PulsePosition1 - (DelayPulse[1] + 15 - DelayPulse[0]), SendRandom);
                Singal1 = true;
                Count1 = 0;
                if (sendCode == 1) {
                    APD1_Code1_count++;
                } else {
                    APD1_Code0_count++;
                }
                out.write("1 Round: " + i + "\t" + "Time: " + APD1_List.get(APD1_Index - 1).getTime() + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "SendCode: " + sendCode + "\r\n");
                //将时间数据与位置记录写入TXT文件
            } else if (Count1 == 1 && PulsePosition1 < (DelayPulse[1] + 15 - DelayPulse[0])) {
                Count1 = 0;
                APD1_PositionErrorCount++;
                // out.write("1 Round: " + i + "\t" + "Time: " + APD1_List.get(APD1_Index-1).getTime() + "\t" + "LightPulse: " + PulsePosition1 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
            };
            if (Count1 > 1) {
                APD1_PulseCountError++;
                Count1 = 0;
            };

            if ((Count2 == 1) && (PulsePosition2 >= (DelayPulse[1] + 15 - DelayPulse[0]))) {
                Singal2 = true;
                Count2 = 0;
                int sendCode = decodeSend(PulsePosition2, PulsePosition2 - (DelayPulse[1] + 15 - DelayPulse[0]), SendRandom);
                if (sendCode == 1) {
                    APD2_Code1_count++;
                } else {
                    APD2_Code0_count++;
                }
                out.write("2 Round: " + i + "\t" + "Time: " + APD2_List.get(APD2_Index - 1).getTime() + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "SendCode: " + sendCode + "\r\n");//将时间数据写入TXT文件
            } else if (Count2 == 1 && PulsePosition2 < (DelayPulse[1] + 15 - DelayPulse[0])) {
                Count2 = 0;
                APD2_PositionErrorCount++;
                //  out.write("2 Round: " + i + "\t" + "Time: " + APD2_List.get(APD2_Index-1).getTime() + "\t" + "LightPulse: " + PulsePosition2 + "\t" + "PositionErrorCount" + "\t" + "\r\n");
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
                    int sendCode = decodeSend(PulsePosition1, PulsePosition1 - (DelayPulse[1] + 15 - DelayPulse[0]), SendRandom);
                    if (sendCode == 0) {
                        code.write("APD1: " + 0 + "\t" + sendCode + "\r\n");
                    } else {
                        CodeError++;
                        code.write("APD1: " + 0 + "\t" + sendCode + " ERROR CODE!" + "\r\n");
                    }
                } else {
                    int sendCode = decodeSend(PulsePosition2, PulsePosition2 - (DelayPulse[1] + 15 - DelayPulse[0]), SendRandom);
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

        for (int c = 0; c < 128; c++) {
            staCountOut.write(c + "\t" + staCountAPD1[c] + "\t" + c + "\t" + staCountAPD2[c] + "\n");
        }
        staCountOut.flush();
        staCountOut.close();

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

    public int[] RandomReadTest() throws FileNotFoundException, IOException {
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

    public int TimeSync(TimeEventList SyncListS, TimeEventList SyncListR, int indexS, int indexR, long GPSDelayTime) {
        int indexSend = indexS;
        long STime = SyncListS.get(indexSend).getTime();
        long RTime = SyncListR.get(indexR).getTime() - GPSDelayTime;
        int SendOffset = 0;
        //long DTime=0;
        while (Math.abs(STime - RTime) > 10000000) {
            if ((STime - RTime) > 50000000) {
                SendOffset = -1;
                // System.out.println("Receive Sync fail! " + "\t" + "Send Sync Time: " + STime + "\t" + "Receive Sync Time: " + RTime);
                break;
            }
            indexSend++;
            SendOffset++;
            if (indexSend > SyncListS.size()) {
                break;
            }
            STime = SyncListS.get(indexSend).getTime();
            //  System.out.println("Send Sync Time: " + STime + "\t" + "Receive Sync Time: " + RTime);
        }
        return SendOffset;
    }

    public int[] SendRandomRead(DataInputStream dis, int Offset, int len) throws FileNotFoundException, IOException {
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

    public int[] ReceiveRandomRead(DataInputStream Ran, int ROffset, int Rlen) throws FileNotFoundException, IOException {
        //提取发射端随机数，存到一个128个元素的整型数组当中

        byte[] R = new byte[Rlen];
        int[] RrandomList = new int[7];
        int delaypulse[] = new int[2];

        Ran.read(R, ROffset, Rlen);
        for (int i = 0; i < 7; i++) {
            if (((R[0] >>> i) & 0x01) == 0x01) {
                RrandomList[i] = 1;
            } else {
                RrandomList[i] = 0;
            }
        }
        delaypulse[0] = (RrandomList[0] + RrandomList[1] * 2 + RrandomList[2] * 4 + RrandomList[3] * 8);
        delaypulse[1] = RrandomList[4] * 16 + RrandomList[5] * 32 + RrandomList[6] * 64;
        return delaypulse;
    }

    public static int decodeSend(int lightPulsePosition, int lightDelay, int[] randamSend) {
        //根据光脉冲位置和发射随机数计算发射端的成码
        int sendCode = randamSend[lightPulsePosition] ^ randamSend[lightDelay];
        return sendCode;
    }

    public void TimeDataTestMode() throws IOException, DeviceException {
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

    public void TimeScan(TimeEventList SyncList, TimeEventList APD1List, TimeEventList APD2List, int DelayTime1, int DelayTime2, int ScanStep, int TimeGate) throws IOException {
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

    public int APDwriteTxtTestMode(TimeEventList SyncList, TimeEventList APD1_List, TimeEventList APD2_List, String s, int DelayTime1, int DelayTime2, int TimeGate) throws IOException {

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
            int[] randomSend = SendRandomRead(randomData, 0, 16);//读取此round的发射端随机数
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

//    private int[] initCount(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        
//    }
}
