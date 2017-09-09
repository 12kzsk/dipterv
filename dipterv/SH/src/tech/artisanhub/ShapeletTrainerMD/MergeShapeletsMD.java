package tech.artisanhub.ShapeletTrainerMD;


import org.jfree.util.ArrayUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MergeShapeletsMD {
/*
   public ArrayList<Shapelet2> mergeShapelets(ArrayList<Shapelet2> shapelets,int requiredClassSize){


       ArrayList<Shapelet2> mergedShapeletsOut = new ArrayList<Shapelet2>();

       Shapelet2 currentShapelet = null;
       Iterator<Shapelet2> iterator = shapelets.iterator();

       int count = shapelets.size()/requiredClassSize;

       ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
       List<Double> currentRow;
       Double classVal;
       int rawSize;
       while (iterator.hasNext()){
           currentShapelet = iterator.next();
           if (count>0){
               currentRow = new ArrayList<Double>(Arrays.asList(currentShapelet.rawContent));
               rawSize = currentRow.size() -1;
               classVal = currentRow.remove(rawSize);
               currentRow.add(rawSize,new Double(currentShapelet.seriesId));
               currentRow.add(rawSize+1,new Double(currentShapelet.startPos));
               currentRow.add(rawSize+2,new Double(classVal));
               temp.add(shapelets.size()/requiredClassSize - count, new ArrayList<Double>(currentRow));
               count --;
           }else {
               count = shapelets.size()/requiredClassSize;
               mergedShapeletsOut.add(new Shapelet2(temp));
               temp = new ArrayList<ArrayList<Double>>();
           }

       }
       mergedShapeletsOut.add(new Shapelet2(temp));
       return mergedShapeletsOut;
   }*/
}
