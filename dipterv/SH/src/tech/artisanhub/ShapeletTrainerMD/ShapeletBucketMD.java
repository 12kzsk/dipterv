package tech.artisanhub.ShapeletTrainerMD;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jawadhsr on 8/14/16.
 */
public class ShapeletBucketMD{

    private Set<ShapeletMD> shapletSet ;
    private int classValue ;

    ShapeletBucketMD(int value){

        shapletSet = new HashSet<ShapeletMD>();
        this.classValue = value;
    }

    public  void put(ShapeletMD shapelet){

        this.shapletSet.add(shapelet);
    }

    public int getClassValue(){
        return this.classValue;
    }

    public Set<ShapeletMD> getShapeletSet(){
        return this.shapletSet;
    }
}
