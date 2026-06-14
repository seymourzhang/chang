package com.chang.coprocessor;

import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;

public abstract class RegionObserverCustom implements RegionCoprocessor, RegionObserver {
}
