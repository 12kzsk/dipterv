package tech.artisanhub.Visualizer;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rnavagamuwa on 8/30/16.
 */
public class TimeSeriesVisualizer {
    private static final String EVENT_1 = "dataset/importantShapeletsEvent1.txt";
    private static final String EVENT_2 = "dataset/importantShapeletsEvent2.txt";
    private static final String COMPLETE_DATA_SET = "dataset/occupancy_time_values.csv";
    private static int event1StartVal;
    private static int event2StartVal;
    private static int event1EndVal;
    private static int event2EndVal;

    public static void main(String args[]) throws IOException {

        HashMap event_1_map = filterAndSortShapeletsForEvent1(EVENT_1);
        HashMap event_2_map = filterAndSortShapeletsForEvent2(EVENT_2);
        System.out.println("2 events are filtered and sorted");

        HashMap datasetMap = row_time_values(COMPLETE_DATA_SET);


        BarChart_AWT chart1 = new BarChart_AWT("Shapelet Learner", "Time Series stats",createDatasetForEvent1(event_1_map,datasetMap));
        chart1.pack( );
        RefineryUtilities.centerFrameOnScreen( chart1 );
        chart1.setVisible( true );

        BarChart_AWT chart2 = new BarChart_AWT("Shapelet Learner", "Time Series stats",createDatasetForEvent2(event_2_map,datasetMap));
        chart2.pack( );
        RefineryUtilities.centerFrameOnScreen( chart2 );
        chart2.setVisible( true );
    }

    private static HashMap row_time_values(String COMPLETE_DATA_SET) throws IOException {

        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(COMPLETE_DATA_SET));
        Instances data = loader.getDataSet();

        HashMap hm = new HashMap();
        // Put elements to the map

        for (int i=0;i<data.numInstances();i++){
            hm.put((int)data.instance(i).value(0),data.instance(i).value(1));
        }

        return hm;
    }

    private static HashMap filterAndSortShapeletsForEvent1(String EVENT_1) throws IOException {

        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(EVENT_1));
        Instances data = loader.getDataSet();

        for (int i=0;i<data.numInstances();i++){
            if (data.instance(i).value(0)==1 && data.instance(i).value(1)==3){
                continue;
            } else {
                data.delete(i);
            }
        }

        int n = data.numInstances();
        Instance temp = null;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {

                if (data.instance(j-1).value(2)> data.instance(j).value(2)) {
                    temp = data.instance(j-1);
//                    ((Object) data).set(j-1,data.instance(j));
//                    ((Object) data).set(j,temp);
//                    data.set(j-1,data.instance(j)); //original TODO
//                   data.set(j,temp);				
                }

            }
        }

        HashMap hm = new HashMap();
        // Put elements to the map

        for (int i=0;i<data.numInstances();i++){
            hm.put((int)data.instance(i).value(2),1);
        }

        event1StartVal = (int)data.instance(0).value(2);
        event1EndVal =(int) data.instance(data.numInstances()-1).value(2);
        return hm;
    }

    private static HashMap filterAndSortShapeletsForEvent2(String EVENT_2) throws IOException {

        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(EVENT_2));
        Instances data = loader.getDataSet();

        for (int i=0;i<data.numInstances();i++){
            if (data.instance(i).value(0)==3 && data.instance(i).value(1)==2){
                continue;
            } else {
                data.delete(i);
            }
        }

        int n = data.numInstances();
        Instance temp = null;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {

                if (data.instance(j-1).value(2)> data.instance(j).value(2)) {
                    temp = data.instance(j-1);
//                    data.set(j-1,data.instance(j));
//TODO                    data.set(j,temp);
                }

            }
        }

        HashMap hm = new HashMap();
        // Put elements to the map

        for (int i=0;i<data.numInstances();i++){
            hm.put((int)data.instance(i).value(2),1);
        }
        event2StartVal = (int)data.instance(0).value(2);
        event2EndVal =(int) data.instance(data.numInstances()-1).value(2);
        return hm;
    }

    private static CategoryDataset createDatasetForEvent1(HashMap event,HashMap completedataset)
    {

        final DefaultCategoryDataset dataset1 = new DefaultCategoryDataset( );



        for (int i=event1StartVal;i<=event1EndVal;i++){
              if (event.containsKey(i))
                  dataset1.addValue(100,"event1", completedataset.get(i).toString());
              else {
                dataset1.addValue(0,"event1", completedataset.get(i).toString());
              }
        }

        return dataset1;
    }

    private static CategoryDataset createDatasetForEvent2(HashMap event,HashMap completedataset)
    {

        final DefaultCategoryDataset dataset1 = new DefaultCategoryDataset( );



        for (int i=event2StartVal;i<=event2EndVal;i++){
            if (event.containsKey(i))
                dataset1.addValue(100,"event2", completedataset.get(i).toString());
            else {
                dataset1.addValue(0,"event2", completedataset.get(i).toString());
            }
        }

        return dataset1;
    }
}
