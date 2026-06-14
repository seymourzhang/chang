package com.chang.util.source;

import java.util.Map;
import java.util.function.Function;

public interface InputSource<T, R> extends Base {
   void InPut(OutputSource var1, Function<T, R> var2) throws Exception;

   Map<String, Object> getSourceExParm();
}
