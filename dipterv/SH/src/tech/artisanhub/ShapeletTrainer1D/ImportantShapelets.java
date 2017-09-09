package tech.artisanhub.ShapeletTrainer1D;

import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jawadhsr on 8/14/16.
 */
public class ImportantShapelets {

	//minden oszt�lyhoz val� legfontosabb shapeletet adja vissza (pl. 3 oszt�lyhoz 3 shapeletet)
    public ArrayList<Shapelet> GetImportantShapelets(ArrayList<Shapelet> shapelets, Instances dataSet,ArrayList<Integer> classValues) {
        ArrayList<Shapelet> shapeletsArr = new ArrayList<Shapelet>();
        ArrayList<Double> classValProbs = new ArrayList<Double>();
        Map<Integer, ShapeletBucket> shapeletBuckets = new HashMap<Integer, ShapeletBucket>(); //1 shapeletBucket <--> 1 oszt�lyhoz tartoz� shapeletek halmaza
        System.out.println();
        
        for (int i = 0; i < classValues.size(); i++) {
            ShapeletBucket temp = new ShapeletBucket(classValues.get(i));

            classValProbs.add(findProb(dataSet, classValues.get(i))); //array with probs of classes
            shapeletBuckets.put(classValues.get(i), temp);
            //remember Above can be optimized.
        }
        Map<Integer,Double> clasNprob = new HashMap<Integer, Double>(); //int = class id, double = probability of the legjellemz�bb class

      //egy adott oszt�lyhoz kigy�jti azokat a shapeleteket, amelyikekre � legink�bb a jellemz� volt
      //oszt�lyonk�nt van 1-1 shap.bucket
        for (Shapelet s : shapelets) {
            // Size of MaxProbClassVal is always one as it returns only a key value pair.
            //where key is the class value and value is the probability.
            for(Integer val : MaxProbClassVal(s).keySet()) {

                clasNprob.put(val, MaxProbClassVal(s).get(val));
                // clas = Integer.parseInt(clasNprob.keySet().toArray()[0].toString());
                shapeletBuckets.get(val).put(s);
            }

        }
        Map <Integer,Map<Shapelet,Double>> shapeDiff = new HashMap<Integer, Map<Shapelet, Double>>();
        int i=0;
        for (int clas : classValues) {
            Map<Shapelet,Double> temp = new HashMap<Shapelet, Double>();
            double aVal = classValProbs.get(i);
            i++;
            for (Shapelet s : shapeletBuckets.get(clas).getShapeletSet()) {

                double val = clasNprob.get(clas);
                temp.put(s, val - aVal);
                //differences[clas][s.seriesId] = Math.abs(/*Here the prob(class Val) of shapelt has to be included. */-classValProbs.get(clas));
                // this has to be changed. The above is wrong.
            }
            shapeDiff.put(clas,temp); //class �s a class sh-ekt�l val� t�vols�gai

            Map<Shapelet,Double> tmp = shapeDiff.get(clas);
            // Done. Now Test the values.
            Shapelet newShape = GetMinDifShape(tmp); //min. t�vols�g� sh (az oszt�lyhoz k�pest)
            // Have to change this heavily.
            shapeletsArr.add(newShape);
        }


        return shapeletsArr;

    }

    //kiveszi azt a shapeletet, amihez a legkisebb t�vols�g tartozik
    private Shapelet GetMinDifShape(Map<Shapelet,Double> shapeDiffs) {
        Shapelet shapelet = null; // = new Shapelet(minDiffs);
        Double minVal = Double.MAX_VALUE;
        for(Map.Entry<Shapelet,Double> sd : shapeDiffs.entrySet()){
            if(minVal > sd.getValue()){
                minVal = sd.getValue();
                shapelet = sd.getKey();
            }
        }

        return shapelet;
    }



    private Map<Integer,Double> MaxProbClassVal(Shapelet shaplet) {
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

    private double findProb(Instances data, int classVal) {
        int count = 0;
/*        for (int i = 0; i < data.size(); i++) {
            if ((int)(data.get(i).classValue()) == classVal) {
                count++;
            }
        }
*/
        for (int i = 0; i < data.numInstances(); i++) {
            if ((int)(data.instance(i).classValue()) == classVal) {
                count++;
            }
        }

//      double var =  count*1.0/data.size();
        double var =  count*1.0/data.numInstances();
        System.out.println("Probability of the classvalue "+classVal+" is "+ var );
        return var;

    }



}
