package com.chang.coprocessor;

import org.apache.hadoop.hbase.coprocessor.MasterCoprocessor;
import org.apache.hadoop.hbase.coprocessor.MasterObserver;

public abstract class MasterObserverCustom implements MasterCoprocessor, MasterObserver {
}
