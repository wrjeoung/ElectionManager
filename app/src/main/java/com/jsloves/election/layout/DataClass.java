package com.jsloves.election.layout;

/**
 * Created by yeon1 on 2015-08-06.
 */
public class DataClass {

    public int seq;
    public String organ;
    public String region;

    public DataClass(int seq, String region, String organ) {
        this.seq = seq;
        this.region = region;
        this.organ = organ;
    }
}
