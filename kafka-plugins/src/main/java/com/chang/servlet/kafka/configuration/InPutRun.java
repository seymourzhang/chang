package com.chang.servlet.kafka.configuration;

import cn.hutool.core.util.ObjectUtil;
import com.chang.servlet.kafka.InputSource;
import com.chang.servlet.kafka.OutputSource;
import com.chang.servlet.kafka.common.ManageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class InPutRun implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(InPutRun.class);
    private OutputSource outputSource;
    private String inputSourceName;

    public InPutRun(OutputSource outputSource, String inputSourceName) {
        this.outputSource = outputSource;
        this.inputSourceName = inputSourceName;
    }

    public void run() {

        InputSource inputSource = ManageSource.getInputSource(this.inputSourceName);
        if (ObjectUtil.isNull(inputSource)) {
            log.error("[Source] inputSource is null KeyName: {}", this.inputSourceName);
            throw new RuntimeException("[Source] inputSource is null KeyName: " + this.inputSourceName);
        } else {
            try {
                inputSource.InPut(this.outputSource, (Function)null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            while(true) {
                try {
                    Thread.sleep(3600000L);
                } catch (InterruptedException var3) {
                    throw new RuntimeException(var3);
                }
            }
        }
    }
}