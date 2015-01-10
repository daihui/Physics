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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author 戴辉
 */
public class DPSTimeData {
    
    public static void main(String[] args) throws IOException, DeviceException {
        TimeDataTest();
       //writeTest();
       // int[] randomSend= RandomReadTest();
//        int sendCode=decodeSend(111, 96, randomSend);
//        System.out.println(sendCode);
        
    }
    
    public static void TimeDataTest() throws IOException, DeviceException{
        File pxiFile = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-8\\receive\\20150108211115-R-APD2-2_时间测量数据.dat");
        File caliFile = null;
        TimeEventLoader pxiLoader = new PXI40PS1Loader(pxiFile, caliFile);
        TimeEventSegment pxiSegment = TimeEventDataManager.loadTimeEventSegment(pxiLoader);
        
        TimeEventList GPSList = pxiSegment.getEventList(0);
        TimeEventList SyncList = pxiSegment.getEventList(1);
        TimeEventList APD2List = pxiSegment.getEventList(2);
        TimeEventList APD1List = pxiSegment.getEventList(3);
        System.out.println("GPS: " + GPSList.size() / 60 + "\t" + GPSList.size());
        System.out.println("Sync: " + SyncList.size() / 60 + "\t" + SyncList.size());
        System.out.println("APD1: " + APD1List.size() / 60 + "\t" + APD1List.size());
        System.out.println("APD2: " + APD2List.size() / 60 + "\t" + APD2List.size());
         
       // writeTxt(GPSList,new String("GPS"));
       // writeTxt(SyncList,new String("Sync"));
       // writeTxt(APD1List,new String("APD1"));
        APDwriteTxt(SyncList,APD1List,APD2List,new String("APD 数据分析"),98575000);
        //APDwriteTxt(SyncList,APD2List,new String("APD2"),98575000);
       
        
        
//        for(int i=0;i<SyncList.size();i++){
//             System.out.println("Round: "+ i + "Sync Time: " + SyncList.get(i).getTime() + "\t" );
//             
//        }
        
//        for(int i=0;i<APD1List.size();i++){
//             System.out.println("APD1: " + (APD1List.get(i+1).getTime()-APD1List.get(i).getTime())/1000.0 + "\t" );
//        }
    }
    
    public static void writeTxt(TimeEventList list,String s) throws IOException{
        Date dt = new Date();
        SimpleDateFormat sdt =  new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);
        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\"+s+" "+date+".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(s+"\r\n");
            for(int i=0;i<list.size();i++){
            out.write(list.get(i).getTime() + "\r\n" );//将时间数据写入TXT文件
            // System.out.println("GPS: " + GPSList.get(i) + "\t" );  
        }
            out.flush(); // 把缓存区内容压入文件  
            out.close(); // 最后记得关闭文件  
        } 
    
     public static void APDwriteTxt(TimeEventList SyncList,TimeEventList APD1_List,TimeEventList APD2_List, String s,int DelayTime) throws IOException{
         
        Date dt = new Date();
        SimpleDateFormat sdt =  new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);
        
        int APD1_Index=0,APD2_Index=0;
        int APD1_Code1_count=0, APD2_Code1_count=0;
        int APD1_Code0_count=0, APD2_Code0_count=0;
        int APD1_PositionErrorCount=0, APD2_PositionErrorCount=0;
        int APD1_PulseCountError=0, APD2_PulseCountError=0;
        int[] randomSend= RandomReadTest();
        
        File writename = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\"+s+" "+date+".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(s+"\r\n");
            for(int i=0;i<15000;i++){
                //大round循环
                long APD1_Time=APD1_List.get(APD1_Index).getTime();
                long APD2_Time=APD2_List.get(APD2_Index).getTime();
                long SyncTime=SyncList.get(i).getTime();
                int Count1=0,Count2=0;
               // out.write("Sync round "+i+"\r\n");
                while(APD1_Time<(SyncTime+DelayTime)){
                   //找到此round 中APD1时间事件的第一个脉冲;
                     APD1_Index++;
                      if(APD1_Index>=APD1_List.size()){break;}
                     APD1_Time=APD1_List.get(APD1_Index).getTime();
                };
                while(APD2_Time<(SyncTime+DelayTime)){
                   // 找到此round 中APD2时间事件的第一个脉冲;;
                     APD2_Index++;
                      if(APD2_Index>=APD2_List.size()){break;}
                     APD2_Time=APD2_List.get(APD2_Index).getTime();
                };
           // while(APDTime<(SyncTime+DelayTime+256000)){
                
                    for (int j = 0; j < 128; j++) {
                        //记录128个脉冲的时间事件位置
                         APD1_Time=APD1_List.get(APD1_Index).getTime();
                         APD2_Time=APD2_List.get(APD2_Index).getTime();
                      // System.out.println(1);
                          while(APD1_Time<(SyncTime+DelayTime+j*2000))
                        {
                           // APD1位置标记;
                           Count1++;
                           APD1_Index++;
                           APD1_Time=APD1_List.get(APD1_Index).getTime();
                        } ;
                        
                         while(APD2_Time<(SyncTime+DelayTime+j*2000))
                        {
                          // APD2位置标记;
                           Count2++;
                           APD2_Index++;
                           APD2_Time=APD2_List.get(APD2_Index).getTime();
                        } ;
                        
                         if((Count1==1)&&(j>=15)){
                             int sendCode=decodeSend(j, j-15, randomSend);
                             Count1=0;
                             if(sendCode==1)
                                 APD1_Code1_count++;
                             else
                                 APD1_Code0_count++;
                             out.write("1 Round: "+i+"\t"+"Time: "+APD1_Time/1000 +"\t"+"LightPulse: "+j+"\t"+"SendCode: "+sendCode+"\r\n");
                             //将时间数据与位置记录写入TXT文件
                            }
                         else if(Count1==1&&j<15){ 
                             Count1=0;
                             APD1_PositionErrorCount++;
                             out.write("1 Round: "+i+"\t"+"Time: "+APD1_Time/1000+"\t"+"LightPulse: "+j+"\t"+"PositionErrorCount"+"\t"+"\r\n");
                            };
                        if(Count1>1){APD1_PulseCountError++;Count1=0;};
                        
                        if((Count2==1)&&(j>=15)){
                            Count2=0;
                             int sendCode=decodeSend(j, j-15, randomSend);
                             if(sendCode==1)
                                 APD2_Code1_count++;
                             else
                                 APD2_Code0_count++;
                             out.write("2 Round: "+i+"\t"+"Time: "+APD2_Time/1000 +"\t"+"LightPulse: "+j+"\t"+"SendCode: "+sendCode+"\r\n");//将时间数据写入TXT文件
                            }
                         else if(Count2==1&&j<15){ 
                             Count2=0;
                             APD2_PositionErrorCount++;
                             out.write("2 Round: "+i+"\t"+"Time: "+APD2_Time/1000 +"\t"+"LightPulse: "+j+"\t"+"PositionErrorCount"+"\t"+"\r\n");
                            };
                        if(Count2>1){APD2_PulseCountError++;Count2=0;};
                     
                        if(APD1_Index>=APD1_List.size()|APD2_Index>=APD2_List.size()){break;}

                    }
           
            if(APD1_Index>=APD1_List.size()|APD2_Index>=APD2_List.size()){break;}
        } 
            out.write("APD1: Code 1 count:"+APD1_Code1_count+"\t"+"Code 0 count:"+APD1_Code0_count+"\t"+"Position Error: "+APD1_PositionErrorCount+"\t"+"Count Error: "+APD1_PulseCountError+"\r\n");
            out.write("APD2: Code 1 count:"+APD2_Code1_count+"\t"+"Code 0 count:"+APD2_Code0_count+"\t"+"Position Error: "+APD2_PositionErrorCount+"\t"+"Count Error: "+APD2_PulseCountError+"\r\n");
            //写入编码统计数据、脉冲位置误码、光子计数误码
            out.flush(); // 把缓存区内容压入文件  
            out.close(); // 关闭文件  
     }
           
    public static int[] RandomReadTest() throws FileNotFoundException, IOException {
        //提取发射端随机数，存到一个128个元素的整型数组当中
         Date dt = new Date();
        SimpleDateFormat sdt =  new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdt.format(dt);
        File randomdata = new File("G:\\DPS数据处理\\DPS实验数据\\2015-1-8\\send\\20150108211115apd2s01_随机数.dat");
        File randomdatTXT =new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\"+date+"sendRandomdata.txt");
        FileInputStream in = new FileInputStream(randomdata);  
                DataInputStream dis=new DataInputStream(in); 
                byte[] b=new byte[16];
                int[] randomList= new int[128];
                
                dis.read(b, 0, 16);
                for (int i = 0; i < 128; i++) {
                    if(((b[(i/8)] >>> (7-(i % 8)))&0x01)==0x01){
                        randomList[i]=0;
                    }else randomList[i]=1;
                    
                }
          randomdatTXT.createNewFile(); // 创建新文件  
          BufferedWriter out = new BufferedWriter(new FileWriter(randomdatTXT));
           for (int i = 0; i < 128; i++) {
                      out.write(randomList[i]+"\r\n");
                }
          out.flush();
          out.close();
          return randomList;
    }
   
    public static int decodeSend(int lightPulsePosition, int lightDelay,int[] randamSend){
        //根据光脉冲位置和发射随机数计算发射端的成码
        int sendCode = randamSend[lightPulsePosition]^randamSend[lightDelay];
        return sendCode;
    }

    
}
