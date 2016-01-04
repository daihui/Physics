package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hwaipy
 */
public class Process {

    public static void main(String[] args) throws IOException, DeviceException {
        String id = "20151105230413";
        String index = "1";
        File path = new File("G:\\unpack");
        long delay1 = 2389900l;
        long delay2 = 2378900l;
        Experiment experiment = new Experiment(id, index, path);
        experiment.setMask((byte) 0x00);
        experiment.loadData();
        System.out.println("load data finshed!");
        experiment.sync(delay1, delay2);
        System.out.println("sync finshed!");
        experiment.filterAndMerge(1000, 258000);
        System.out.println("merge finshed!");
        ArrayList<Decoder.Entry> result = experiment.decoding(800);
        System.out.println("decode finshed!");
        ResultParser resultParser = new ResultParser(result);
        resultParser.ResultOutFile(result, id);
        resultParser.ResultStatistics(result, id);
       // resultParser.ResultbyGate(result, experiment.getBobQRNGList(), id);
        experiment.test();

    }
}
