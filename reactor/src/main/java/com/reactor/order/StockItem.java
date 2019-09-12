package com.reactor.order;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class StockItem {

    private final AtomicLong atomicLong = new AtomicLong(0);

    public void store(long n){
        atomicLong.accumulateAndGet(n,(pre,mout)->pre+mout);

    }


    public long remove(long n){
        class RemoveData{
            long remove;
        }
        RemoveData removeData = new RemoveData();
        atomicLong.accumulateAndGet(n,(pre,mount)->pre>=n?pre-(removeData.remove=mount):pre-(removeData.remove=0L));
        return removeData.remove;
    }
}
