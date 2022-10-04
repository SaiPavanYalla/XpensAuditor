package com.xa.xpensauditor;

import java.util.Collection;
import java.util.Iterator;

public class SumTrans {
    public Integer sum;

    public SumTrans(){
        sum=0;
    }

    public Integer computeSum(Collection<String> str){
        for(Iterator<String> entry = str.iterator(); entry.hasNext();){

            String value = entry.next();
            sum = sum+ Integer.parseInt(value);
            //Log.wtf("afSDFsdfEWSFSDcWEESFsedfSEDFwesedf",sum.toString());

        }
        return sum;
    }
}
