package com.jenkov;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class Vol {
   
    private static final Map<String,String> map = new HashMap<>();
    private static volatile boolean write = false;
    private static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private static int count = 0;

    @Setup(Level.Trial)
    public void doSetup() {
        count = 0;
        write = false;
        for(int i=0;i<10000;i++){
            map.put(""+i,""+(2*i));
        }
        
        ses.scheduleAtFixedRate(new Runnable(){
        
            @Override
            public void run() {
                Map<String,String> tmp = new HashMap<>();
                for(int i=0;i<10000;i++){
                    map.put(""+i,""+(2*i));
                }
                write = true;
                synchronized(map) {
                    map.clear();
                    map.putAll(tmp);
                }
                write = false;
                count++;
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @TearDown(Level.Trial)
    public void doTearDown() {
        ses.shutdownNow();
        map.clear();
        System.out.println("Vol counter: "+count);
    }

    public String getValue(String s){
        if (write) {
            synchronized(map){
                return map.get(s);
            }
        } else {
            return map.get(s); 
        }   
    } 
}