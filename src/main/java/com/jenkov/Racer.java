package com.jenkov;

import java.util.HashMap;
import java.util.Map;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class Racer {
   
    private static final Map<String,String> map = new HashMap<>();

    @Setup(Level.Trial)
    public void doSetup() {
        for(int i=0;i<10000;i++){
            map.put(""+i,""+(2*i));
        }
    }

    @TearDown(Level.Trial)
    public void doTearDown() {
        map.clear();
    }

    public String getValue(String s){
        return map.get(s);
    }
  
}