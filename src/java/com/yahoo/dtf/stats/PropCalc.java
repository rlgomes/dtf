package com.yahoo.dtf.stats;

public class PropCalc {
    private long accValue = 0;

    private long occurences = 0;

    private long maxValue = 0;

    private long minValue = 0;

    public void addResult(long value) {
        accValue += value;
        occurences++;

        if (value > maxValue)
            maxValue = value;

        if (value < minValue)
            minValue = value;
    }

    public long getAccValue() {
        return accValue;
    }

    public void setAccValue(long accValue) {
        this.accValue = accValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getOccurences() {
        return occurences;
    }

    public void setOccurences(long occurences) {
        this.occurences = occurences;
    }
}