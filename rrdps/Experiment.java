package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.StreamTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class Experiment {

    private static final boolean DEBUG = false;
    private static final int CHANNEL_APD1 = 2;
    private static final int CHANNEL_APD2 = 3;
    private static final Map<String, String[]> FILENAME_MAP = new HashMap<>();

    static {
//        //发射端时间测量数据，发射端随机数数据，接收端时间测量数据，接收端随机数数据，(接收端稳相数据)
        //20150131
//        FILENAME_MAP.put("20150130125829", new String[]{"20150130125829-S-固定-2_时间测量数据.dat", "20150130125829-S-固定-2_发射端随机数.dat", "20150130125829-R-固定-单路-APD2-2_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130125949", new String[]{"20150130125949-S-固定-3_时间测量数据.dat", "20150130125949-S-固定-3_发射端随机数.dat", "20150130125949-R-固定-单路-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130130214", new String[]{"20150130130214-S-随机-4_时间测量数据.dat", "20150130130214-S-随机-4_发射端随机数.dat", "20150130130214-R-随机-单路-APD1-4_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130130316", new String[]{"20150130130316-S-随机-5_时间测量数据.dat", "20150130130316-S-随机-5_发射端随机数.dat", "20150130130316-R-随机-单路-APD2-5_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150123003253", new String[]{"20150123003253-S-随机-3_时间测量数据.dat", "20150123003253-S-随机-3_发射端随机数.dat", "20150123003253-R-随机-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130134530", new String[]{"20150130134530-S-固定-PC12-3_时间测量数据.dat", "20150130134530-S-固定-PC12-3_发射端随机数.dat", "20150130134530-R-固定-PC12-APD1-3_时间测量数据.dat", "20150130134530-R-固定-PC12-APD1-3_接收端随机数.dat"});
//        FILENAME_MAP.put("20150130134722", new String[]{"20150130134722-S-随机-PC12-4_时间测量数据.dat", "20150130134722-S-随机-PC12-4_发射端随机数.dat", "20150130134722-R-随机-PC12-APD1-4_时间测量数据.dat", "20150130134722-R-随机-PC12-APD1-4_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128185754", new String[]{"20150128185754-S-固定-7_时间测量数据.dat","20150128185754-S-固定-7_发射端随机数.dat","20150128185754-R-固定-PC-APD1-7_时间测量数据.dat","20150128185754-R-固定-PC-APD1-7_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128185603", new String[]{"20150128185603-S-固定-6_时间测量数据.dat", "20150128185603-S-固定-6_发射端随机数.dat", "20150128185603-R-固定-PC1-APD2-6_时间测量数据.dat", "20150128185603-R-固定-PC1-APD2-6_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128190139", new String[]{"20150128190139-S-固定-8_时间测量数据.dat", "20150128190139-S-固定-8_发射端随机数.dat", "20150128190139-R-固定-PC12-APD2-8_时间测量数据.dat", "20150128190139-R-固定-PC12-APD2-8_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128190431", new String[]{"20150128190431-S-固定-9_时间测量数据.dat", "20150128190431-S-固定-9_发射端随机数.dat", "20150128190431-R-固定-PC12-APD1-9_时间测量数据.dat", "20150128190431-R-固定-PC12-APD1-9_接收端随机数.dat"});
//        FILENAME_MAP.put("20150127174646", new String[]{"20150127174646-S-固定-10_时间测量数据.dat","20150127174646-S-固定-10_发射端随机数.dat","20150127174647-R-PC16-APD2-10_时间测量数据.dat","20150127174647-R-PC16-APD2-10_接收端随机数.dat"});
//        FILENAME_MAP.put("20150127174833", new String[]{"20150127174833-S-固定-11_时间测量数据.dat","20150127174833-S-固定-11_发射端随机数.dat","20150127174833-R-PC16-APD1-11_时间测量数据.dat","20150127174833-R-PC16-APD1-11_接收端随机数.dat"});

//        //20150206第二次
//        FILENAME_MAP.put("20150206235224", new String[]{"20150206235224-s-固定随机数单路1_时间测量数据.dat", "20150206235224-s-固定随机数单路1_发射端随机数.dat", "20150206235224-R-固定-APD1-1_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150206235449", new String[]{"20150206235449-s-固定随机数单路2_时间测量数据.dat", "20150206235449-s-固定随机数单路2_发射端随机数.dat", "20150206235449-R-固定-APD2-2_时间测量数据.dat", "rnd-00.dat"});
//
//        //2015-2-7第一次
//        FILENAME_MAP.put("20150207162939", new String[]{"20150207162939-s-固定随机数单路1_时间测量数据.dat", "20150207162939-s-固定随机数单路1_发射端随机数.dat", "20150207162939-R-固定-APD2-1_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163135", new String[]{"20150207163135-s-固定随机数单路2_时间测量数据.dat", "20150207163135-s-固定随机数单路2_发射端随机数.dat", "20150207163135-R-固定-APD1-2_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163609", new String[]{"20150207163609-s-真随机数单路4_时间测量数据.dat", "20150207163609-s-真随机数单路4_发射端随机数.dat", "20150207163609-R-随机-APD2-4_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163313", new String[]{"20150207163313-s-真随机数单路3_时间测量数据.dat", "20150207163313-s-真随机数单路3_发射端随机数.dat", "20150207163313-R-随机-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207164011", new String[]{"20150207164011-s-固定随机数2路5_时间测量数据.dat", "20150207164011-s-固定随机数2路5_发射端随机数.dat", "20150207164012-R-固定-PC2-APD2-5_时间测量数据.dat", "20150207164012-R-固定-PC2-APD2-5_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207164339", new String[]{"20150207164339-s固定随机数2路6_时间测量数据.dat", "20150207164339-s固定随机数2路6_发射端随机数.dat", "20150207164340-R-固定-PC2-APD1-6_时间测量数据.dat", "20150207164340-R-固定-PC2-APD1-6_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207171149", new String[]{"20150207171149-s-固定随机数7路14_时间测量数据.dat", "20150207171149-s-固定随机数7路14_发射端随机数.dat", "20150207171149-R-固定-PC-APD1-14_时间测量数据.dat", "20150207171149-R-固定-PC-APD1-14_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207171035", new String[]{"20150207171035-s-固定随机数7路13_时间测量数据.dat", "20150207171035-s-固定随机数7路13_发射端随机数.dat", "20150207171036-R-固定-PC-APD2-13_时间测量数据.dat", "20150207171036-R-固定-PC-APD2-13_接收端随机数.dat"});
        //2015-2-8//稳相测试
//        FILENAME_MAP.put("20150212024655", new String[]{"20150212024655_时间测量数据.dat", "20150212024655_发射端随机数.dat", "20150212024655-PC-APD1-6_时间测量数据.dat", "20150212024655-PC-APD1-6_接收端随机数.dat", "20150212024655-PC-APD1-6_稳相结果.csv", "20150212024655-PC-APD1-6_稳相数据.csv"});
//
//        //2015-2-12
//        FILENAME_MAP.put("20150212224328", new String[]{"20150212224328-S-2_时间测量数据.dat", "20150212224328-S-2_发射端随机数.dat", "20150212224328-单路-固定-APD1-2_时间测量数据.dat", "rnd-00.dat", "20150212224328-单路-固定-APD1-2_稳相结果.csv", "20150212224328-单路-固定-APD1-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150212235016", new String[]{"20150212235016-s-3_时间测量数据.dat", "20150212235016-s-3_发射端随机数.dat", "20150212235016-PC35-固定-APD1-3_时间测量数据.dat", "20150212235016-PC35-固定-APD1-3_接收端随机数.dat", "20150212235016-PC35-固定-APD1-3_稳相结果.csv", "20150212235016-PC35-固定-APD1-3_稳相数据.csv"});
//        FILENAME_MAP.put("20150213003554", new String[]{"20150213003554-s-5_时间测量数据.dat", "20150213003554-s-5_发射端随机数.dat", "20150213003554-PC345-固定-APD1-5_时间测量数据.dat", "20150213003554-PC345-固定-APD1-5_接收端随机数.dat", "20150213003554-PC345-固定-APD1-5_稳相结果.csv", "20150213003554-PC345-固定-APD1-5_稳相数据.csv"});
//        FILENAME_MAP.put("20150213004935", new String[]{"20150213004935-s-6_时间测量数据.dat", "20150213004935-s-6_发射端随机数.dat", "20150213004935-PC13456-随机-APD1-6_时间测量数据.dat", "20150213004935-PC13456-随机-APD1-6_接收端随机数.dat", "20150213004935-PC13456-随机-APD1-6_稳相结果.csv", "20150213004935-PC13456-随机-APD1-6_稳相数据.csv"});
//        FILENAME_MAP.put("20150213011049", new String[]{"20150213011049-s-7_时间测量数据.dat", "20150213011049-s-7_发射端随机数.dat", "20150213011049-PC13456-随机-APD1-7_时间测量数据.dat", "20150213011049-PC13456-随机-APD1-7_接收端随机数.dat", "20150213011049-PC13456-随机-APD1-7_稳相结果.csv", "20150213011049-PC13456-随机-APD1-7_稳相数据.csv"});
//        FILENAME_MAP.put("20150213011520", new String[]{"20150213011520-s-8_时间测量数据.dat", "20150213011520-s-8_发射端随机数.dat", "20150213011520-PC345-随机-APD1-8_时间测量数据.dat", "20150213011520-PC345-随机-APD1-8_接收端随机数.dat", "20150213011520-PC345-随机-APD1-8_稳相结果.csv", "20150213011520-PC345-随机-APD1-8_稳相数据.csv"});
//
//        //2015-2-14正式采数
//        FILENAME_MAP.put("20150214100455", new String[]{"20150214100455-s-pc7-3_时间测量数据.dat", "20150214100455-s-pc7-3_发射端随机数.dat", "20150214100455-PCALL-APD2-3_时间测量数据.dat", "20150214100455-PCALL-APD2-3_接收端随机数.dat", "20150214100455-PCALL-APD2-3_稳相结果.csv", "20150214100455-PCALL-APD2-3_稳相数据.csv"});
//        //2015-2-28
//        FILENAME_MAP.put("20150228232128", new String[]{"20150228232128-s-3_时间测量数据.dat", "20150228232128-s-3_发射端随机数.dat", "20150228232128-R-固定-new-APD2-3_时间测量数据.dat", "rnd-00.dat", "20150228232128-R-固定-new-APD2-3_稳相结果.csv", "20150228232128-R-固定-new-APD2-3_稳相数据.csv"});

         //2015-3-1
        FILENAME_MAP.put("20150301221730", new String[]{"20150301221730-S-1_时间测量数据.dat", "20150301221730-S-1_发射端随机数.dat", "20150301221730-R-固定-old-APD2-1_时间测量数据.dat", "rnd-00.dat", "20150301221730-R-固定-old-APD2-1_稳相结果.csv", "20150301221730-R-固定-old-APD2-1_稳相数据.csv"});
        FILENAME_MAP.put("20150301222829", new String[]{"20150301222829-S-2_时间测量数据.dat", "20150301222829-S-2_发射端随机数.dat", "20150301222829-R-固定-old-APD2-2_时间测量数据.dat", "20150301222829-R-固定-old-APD2-2_接收端随机数.dat", "20150301222829-R-固定-old-APD2-2_稳相结果.csv", "20150301222829-R-固定-old-APD2-2_稳相数据.csv"});

    }
    private final String id;
    private final File path;
    private TimeEventList aliceRandomList;
    private TimeEventList bobRandomList;
    private TimeEventList apd1List;
    private TimeEventList apd2List;
    private TimeEventList apdList;

    public Experiment(String id, File path) {
        this.id = id;
        this.path = path;
    }

    public void loadData() throws IOException, DeviceException {
        String[] fileNames = FILENAME_MAP.get(id);
        if (fileNames == null) {
            throw new RuntimeException();
        }
        File AliceTDCFile = new File(path, fileNames[0]);
        File AliceQRNGFile = new File(path, fileNames[1]);
        File BobTDCFile = new File(path, fileNames[2]);
        File BobQRNGFile = new File(path, fileNames[3]);
        File plrFile = new File(path, fileNames[4]);
        File pldFile = new File(path, fileNames[5]);
        TimeEventSegment aliceSegment = loadTDCFile(AliceTDCFile);
        TimeEventSegment bobSegment = loadTDCFile(BobTDCFile);

        ArrayList<EncodingRandom> aliceQRNGList = loadEncodingQRNGFile(AliceQRNGFile);
        ArrayList<DecodingRandom> bobQRNGList = loadDecodingQRNGFile(BobQRNGFile);
        ArrayList<PhaseLockingResult> phaseLockingResultlist = loadPhaseLockerFile(plrFile, pldFile);
        aliceRandomList = timingQRNG(aliceQRNGList, aliceSegment.getEventList(1));
        bobRandomList = timingQRNG(bobQRNGList, bobSegment.getEventList(1));
        System.out.println(aliceQRNGList.size() + "\t" + bobRandomList.size());
        apd1List = bobSegment.getEventList(CHANNEL_APD1);
        apd2List = bobSegment.getEventList(CHANNEL_APD2);
//        System.out.println(apd1List.size()+"\t"+apd2List.size());
    }

    public void sync(long delay1, long delay2) {
//        TimeEventList aliceGPSList = aliceSegment.getEventList(0);
//        TimeEventList aliceSyncList = aliceSegment.getEventList(1);
//        TimeEventList bobGPSList = bobSegment.getEventList(0);
//        TimeEventList bobSyncList = bobSegment.getEventList(1);
//        long coarseDelay = aliceGPSList.get(0).getTime() - bobGPSList.get(0).getTime();
//        RecursionCoincidenceMatcher syncMatcher = new RecursionCoincidenceMatcher(bobSyncList, aliceSyncList, 1000000, coarseDelay);

        for (int i = 0; i < apd1List.size(); i++) {
            TimeEvent e = apd1List.get(i);
            apd1List.set(new TimeEvent(e.getTime() - delay1, e.getChannel()), i);
        }
        for (int i = 0; i < apd2List.size(); i++) {
            TimeEvent e = apd2List.get(i);
            apd2List.set(new TimeEvent(e.getTime() - delay2, e.getChannel()), i);
        }
    }

    public void filterAndMerge(long before, long after) {
        System.out.println(apd1List.size());
        System.out.println(apd2List.size());
        apd1List = doFilter(apd1List, bobRandomList, before, after);
        apd2List = doFilter(apd2List, bobRandomList, before, after);
        System.out.println(apd1List.size());
        System.out.println(apd2List.size());
        apdList = new MergedTimeEventList(apd1List, apd2List);
        System.out.println("-" + apdList.size());
    }

    public ArrayList<Decoder.Entry> decoding(long gate) {
        Tagger tagger = new Tagger(bobRandomList, apdList, gate);
        ArrayList<Tagger.Entry> tags = tagger.tag();

        Decoder decoder = new Decoder(tags, aliceRandomList, bobRandomList);
        ArrayList<Decoder.Entry> result = decoder.decode();
        return result;
    }

    private TimeEventList doFilter(TimeEventList apdList, TimeEventList bobRandomList, long before, long after) {
        Iterator<TimeEvent> apdIterator = apdList.iterator();
        Iterator<TimeEvent> syncIterator = bobRandomList.iterator();
        TimeEvent syncEvent = syncIterator.next();
        long startTime = syncEvent.getTime() - before;
        long endTime = syncEvent.getTime() + after;
        TimeEvent apdEvent = apdIterator.next();
        StreamTimeEventList newList = new StreamTimeEventList();
        while (true) {
            long time = apdEvent.getTime();
            if (time <= endTime) {
                if (time >= startTime) {
                    newList.offer(apdEvent);
                }
                if (apdIterator.hasNext()) {
                    apdEvent = apdIterator.next();
                } else {
                    apdEvent = null;
                }
            } else {
                if (syncIterator.hasNext()) {
                    syncEvent = syncIterator.next();
                    startTime = syncEvent.getTime() - before;
                    endTime = syncEvent.getTime() + after;
                } else {
                    syncEvent = null;
                }
            }
            if (apdEvent == null || syncEvent == null) {
                break;
            }
        }
        return newList;
    }

    private TimeEventSegment loadTDCFile(File file) throws IOException, DeviceException {
        TimeEventLoader loader = new PXI40PS1Loader(file, null);
        return TimeEventDataManager.loadTimeEventSegment(loader);
    }

    private ArrayList<EncodingRandom> loadEncodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        byte[] b = new byte[16];
        int index = 0;
        ArrayList<EncodingRandom> list = new ArrayList<>();
        while (true) {
            int[] randomList = new int[128];
            int read = input.read(b);
            if (read < 16) {
                break;
            }
            for (int i = 0; i < 128; i++) {
                if (((b[(i / 8)] >>> (7 - (i % 8))) & 0x01) == 0x01) {
                    randomList[i] = 0;
                } else {
                    randomList[i] = 1;
                }
            }
//            System.out.println(Arrays.toString(randomList));
            EncodingRandom encodingRandom = new EncodingRandom(randomList);
            list.add(encodingRandom);
            index++;
        }
//        for (EncodingRandom encodingRandom : list) {
//            System.out.println(encodingRandom);
//        }
        return list;
    }

    private ArrayList<DecodingRandom> loadDecodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        int b;
        ArrayList<DecodingRandom> list = new ArrayList<>();
        while (true) {
            b = input.read();
            if (b == -1) {
                break;
            }
            int[] RrandomList = new int[7];
            int[] delaypulse = new int[2];
            byte R = (byte) b;
            for (int i = 0; i < 7; i++) {
                if (((R >>> i) & 0x01) == 0x01) {
                    RrandomList[i] = 1;
                } else {
                    RrandomList[i] = 0;
                }
            }
            delaypulse[0] = (RrandomList[0] + RrandomList[1] * 2 + RrandomList[2] * 4 + RrandomList[3] * 8);
            delaypulse[1] = RrandomList[4] * 16 + RrandomList[5] * 32 + RrandomList[6] * 64;
            list.add(new DecodingRandom(delaypulse[0], delaypulse[1]));
        }
        return list;
    }

    private <T> TimeEventList timingQRNG(ArrayList<T> QRNGList, TimeEventList timingList) {
        //数据校验
        if (DEBUG) {
            Iterator<TimeEvent> iterator = timingList.iterator();
            TimeEvent t1 = iterator.next();
            while (iterator.hasNext()) {
                TimeEvent t2 = iterator.next();
                long deltaT = t2.getTime() - t1.getTime();
                if (Math.abs(deltaT) > 150000000 && Math.abs(deltaT) < 700000000000l) {
                    throw new RuntimeException();
                }
                t1 = t2;
            }
        }

        int length = Math.min(QRNGList.size(), timingList.size());
        StreamTimeEventList streamTimeEventList = new StreamTimeEventList();
        Iterator<TimeEvent> iterator = timingList.iterator();
        for (int i = 0; i < length; i++) {
            T random = QRNGList.get(i);
            TimeEvent timeEvent;
            if (iterator.hasNext()) {
                timeEvent = iterator.next();
            } else {
                throw new RuntimeException();
            }
            ExtandedTimeEvent<T> ete = new ExtandedTimeEvent<>(timeEvent.getTime(), timeEvent.getChannel(), random);
            streamTimeEventList.offer(ete);
        }
        return streamTimeEventList;
    }

    public ArrayList<PhaseLockingResult> loadPhaseLockerFile(File plrFile, File pldFile) throws FileNotFoundException, IOException {
//        File path = new File("G:\\DPS数据处理\\DPS实验数据\\2015-2-11\\一次一稳");
//        File plrFile = new File(path, "20150212024655-PC-APD1-6_稳相结果.csv");
//        File pldFile = new File(path, "20150212024655-PC-APD1-6_稳相数据.csv");
        ArrayList<PhaseLockingResult> phaseLockingResultlist = new ArrayList<>();;
        BufferedReader plrReader = new BufferedReader(new InputStreamReader(new FileInputStream(plrFile), "GB2312"));
        BufferedReader pldReader = new BufferedReader(new InputStreamReader(new FileInputStream(pldFile), "GB2312"));
        while (true) {
            String[] plrs = readLines(plrReader, 3);
            String[] plds = readLines(pldReader, 24);
            if (plrs == null || plds == null) {
                break;
            }
            PhaseLockingResult phaseLockingResult = parse(plrs, plds);
            phaseLockingResultlist.add(phaseLockingResult);
        }
        return phaseLockingResultlist;
    }

    private String[] readLines(BufferedReader reader, int count) throws IOException {
        String[] results = new String[count];
        for (int i = 0; i < count; i++) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            results[i] = line;
        }
        return results;
    }

    private PhaseLockingResult parse(String[] plrs, String[] plds) {
        String[] split1 = plrs[2].split(" *, *");
        int plResult = Integer.parseInt(split1[6]);

        int countAPD1 = 0;
        int countAPD2 = 0;
        for (String pld : plds) {
            String[] split2 = pld.split(" *, *");
            countAPD1 += Integer.parseInt(split2[3]);
            countAPD2 += Integer.parseInt(split2[4]);
        }
        double plLarge = (countAPD1 + countAPD2) / 24.;
//        System.out.println(plLarge / plResult);
        return new PhaseLockingResult(plResult, plLarge);
    }

    void test() {
        MergedTimeEventList m = new MergedTimeEventList(apd1List, apd2List);
        System.out.println(m.size());
        Iterator<TimeEvent> iterator = m.iterator();
        long t = 0;
        int i = 0;
        while (iterator.hasNext()) {
            TimeEvent next = iterator.next();
            if (next.getTime() < t) {
                System.out.println("wrong");
            } else {
                t = next.getTime();
            }
        }
    }
}
