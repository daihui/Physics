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
        String id = "20150301222829";
        File path = new File("G:\\DPS数据处理\\DPS实验数据\\2015-3-1");
        long delay1 =2495780300l;
        long delay2 =2495774400l;
        Experiment experiment = new Experiment(id, path);
        experiment.loadData();
        experiment.sync(delay1, delay2);
        experiment.filterAndMerge(1000, 258000);
        ArrayList<Decoder.Entry> result = experiment.decoding(800);
        ResultParser resultParser = new ResultParser(result);
        resultParser.ResultOutFile(result, id);
        resultParser.ResultStatistics(result, id);
//        experiment.test();
 
    }
}
