package com.xa.xpensauditor;

import android.icu.number.Precision;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

public class SumTrans {
    public double sum;

    public SumTrans(){
        sum=0.00;
    }

    public Double computeSum(Collection<String> str){
        for(Iterator<String> entry = str.iterator(); entry.hasNext();){

            String value = entry.next();
            sum = sum+ Double.parseDouble((value));
        }
        return sum;
    }
}
