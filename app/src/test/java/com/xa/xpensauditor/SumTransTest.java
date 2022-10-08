package com.xa.xpensauditor;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class SumTransTest {

    SumTrans sumTrans = new SumTrans();

    @Test
    public void testComputeSum(){
        ArrayList<String> collection = new ArrayList<String>() { // anonymous subclass
            {
                add("1");
                add("2");
                add("3");
            }
        };
        assertEquals(Double.valueOf(6), sumTrans.computeSum(collection));
    }

}