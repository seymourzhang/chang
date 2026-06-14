package com.chang.coprocessor;

import org.apache.hadoop.hbase.coprocessor.RegionServerCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionServerObserver;

public abstract class RegionServerObserverCustom implements RegionServerCoprocessor, RegionServerObserver {
}
