package tech.artisanhub.ShapeletTrainerMD;

import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jawadhsr on 8/14/16.
 */
public class ImportantShapeletsMD {
/*
	//minden osztályhoz való legfontosabb shapeletet adja vissza (pl. 3 osztályhoz 3 shapeletet)
    public ArrayList<Shapelet2> GetImportantShapelets(ArrayList<Shapelet2> shapelets, Instances dataSet,ArrayList<Integer> classValues) {
        ArrayList<Shapelet2> shapeletsArr = new ArrayList<Shapelet2>();
        ArrayList<Double> classValProbs = new ArrayList<Double>();
        Map<Integer, ShapeletBucket2> shapeletBuckets = new HashMap<Integer, ShapeletBucket2>(); //1 shapeletBucket <--> 1 osztályhoz tartozó shapeletek halmaza
        System.out.println();
        
        for (int i = 0; i < classValues.size(); i++) {
            ShapeletBucket2 temp = new ShapeletBucket2(classValues.get(i));

            classValProbs.add(findProb(dataSet, classValues.get(i))); //array with probs of classes
            shapeletBuckets.put(classValues.get(i), temp);
            //remember Above can be optimized.
        }
        Map<Integer,Double> clasNprob = new HashMap<Integer, Double>(); //int = class id, double = probability of the legjellemzõbb class

      //egy adott osztályhoz kigyûjti azokat a shapeleteket, amelyikekre õ leginkább a jellemzõ volt
      //osztályonként van 1-1 shap.bucket
        for (Shapelet2 s : shapelets) {
            // Size of MaxProbClassVal is always one as it returns only a key value pair.
            //where key is the class value and value is the probability.
            for(Integer val : MaxProbClassVal(s).keySet()) {

                clasNprob.put(val, MaxProbClassVal(s).get(val));
                // clas = Integer.parseInt(clasNprob.keySet().toArray()[0].toString());
                shapeletBuckets.get(val).put(s);
            }

        }
        Map <Integer,Map<Shapelet2,Double>> shapeDiff = new HashMap<Integer, Map<Shapelet2, Double>>();
        int i=0;
        for (int clas : classValues) {
            Map<Shapelet2,Double> temp = new HashMap<Shapelet2, Double>();
            double aVal = classValProbs.get(i);
            i++;
            for (Shapelet2 s : shapeletBuckets.get(clas).getShapeletSet()) {

                double val = clasNprob.get(clas);
                temp.put(s, val - aVal);
                //differences[clas][s.seriesId] = Math.abs(/*Here the prob(class Val) of shapelt has to be included. *///-classValProbs.get(clas));
                // this has to be changed. The above is wrong.
          /*  }
            shapeDiff.put(clas,temp); //class és a class sh-ektõl való távolságai

            Map<Shapelet2,Double> tmp = shapeDiff.get(clas);
            // Done. Now Test the values.
            Shapelet2 newShape = GetMinDifShape(tmp); //min. távolságú sh (az osztályhoz képest)
            // Have to change this heavily.
            shapeletsArr.add(newShape);
        }


        return shapeletsArr;

    }

    
   */
    //kiveszi azt a shapeletet, amihez a legkisebb távolság tartozik
/*    private Shapelet2 GetMinDifShape(Map<Shapelet2,Double> shapeDiffs) {
        Shapelet2 shapelet = null; // = new Shapelet(minDiffs);
        Double minVal = Double.MAX_VALUE;
        for(Map.Entry<Shapelet2,Double> sd : shapeDiffs.entrySet()){
            if(minVal > sd.getValue()){
                minVal = sd.getValue();
                shapelet = sd.getKey();
            }
        }

        return shapelet;
    }

*/
/*
    private Map<Integer,Double> MaxProbClassVal(Shapelet2 shaplet) {
       ArrayList<ArrayList<Double>> shapeContent = shaplet.contentInMergedShapelets;
        ArrayList<Double> tempArr = new ArrayList<Double>();
        int count = 0;
        for(ArrayList<Double> val : shapeContent){		//shapeContent = arr.list of shapelets (mergedshs)
            tempArr.add(count,val.get(val.size()-1));
            count ++;
        }

        Map <Integer,Double> retValue = new HashMap<Integer, Double>();
        //Arrays.sort(tempArr);
        Map <Double,Integer> classValCount = new HashMap<Double, Integer>();

        for(int i=0;i<tempArr.size();i++){
           if(classValCount.containsKey(tempArr.get(i))){
               Integer val = classValCount.get(tempArr.get(i));
               val ++;
               classValCount.put(tempArr.get(i),val);
           }
            else {
               classValCount.put(tempArr.get(i),1);
           }
        }
        int MaxVal = 0;
        Double MaxKey = 0.0; // classValues
       for (Map.Entry<Double,Integer> val : classValCount.entrySet()){
            if(val.getValue()>MaxVal){
                MaxVal = val.getValue();
                MaxKey = val.getKey();
            }
       }
        Double maxProb = MaxVal*1.0/tempArr.size();
        retValue.put(MaxKey.intValue(),maxProb); //most popular class (id), rate of this
        return retValue;
    }
*/
                /*
    private double findProb(Instances data, int classVal) {
        int count = 0;
/*        for (int i = 0; i < data.size(); i++) {
            if ((int)(data.get(i).classValue()) == classVal) {
                count++;
            }
        }
*/
 /*       for (int i = 0; i < data.numInstances(); i++) {
            if ((int)(data.instance(i).classValue()) == classVal) {
                count++;
            }
        }

//      double var =  count*1.0/data.size();
        double var =  count*1.0/data.numInstances();
        System.out.println("Probability of the classvalue "+classVal+" is "+ var );
        return var;

    }

*/
}
