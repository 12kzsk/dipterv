package tech.artisanhub.ShapeletTrainer1D;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jawadhsr on 8/14/16.
 */
public class ShapeletBucket{

    private Set<Shapelet> shapletSet ;
    private int classValue ;

    ShapeletBucket(int value){

        shapletSet = new HashSet<Shapelet>();
        this.classValue = value;
    }

    public  void put(Shapelet shapelet){

        this.shapletSet.add(shapelet);
    }

    public int getClassValue(){
        return this.classValue;
    }

    public Set<Shapelet> getShapeletSet(){
        return this.shapletSet;
    }
}
