package com.chang.common.util;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class ObjectSizeCalculator {
    
    public static long getObjectSize(Object object) {
        return VM.current().sizeOf(object);
    }
    
    public static String getObjectLayout(Object object) {
        return ClassLayout.parseInstance(object).toPrintable();
    }
}