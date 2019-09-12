package com.reactor.interator;

import java.math.BigInteger;
import java.util.Iterator;

public class GenerateNumbersIterrator implements Iterator {

    private BigInteger current = BigInteger.ZERO;

    private BigInteger num;

    public GenerateNumbersIterrator(BigInteger num){
        this.num=num;
    }
    @Override
    public boolean hasNext() {
        return current.compareTo(num)<0;
    }

    @Override
    public Object next() {
        current=current.add(BigInteger.ONE);
        return current;
    }

    public static void main(String[] args) {
        GenerateNumbersIterrator generateNumbersIterrator = new GenerateNumbersIterrator(BigInteger.valueOf(10L));
        while (generateNumbersIterrator.hasNext()){
            System.out.println(generateNumbersIterrator.next());
        }
    }
}
