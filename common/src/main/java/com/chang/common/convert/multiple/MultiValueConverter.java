package com.chang.common.convert.multiple;

import com.chang.common.Prioritized;

public interface MultiValueConverter<S> extends Prioritized {
   Object convert(S var1, Class<?> var2, Class<?> var3);
}
