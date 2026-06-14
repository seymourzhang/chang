package com.chang.binlog.event.parser.converter;

import com.chang.binlog.tablemeta.TableMetaEntity;
import java.io.Serializable;
import java.util.List;

public class CommonConverterProcessor {
   public String[] convertToString(Serializable[] serializers, List<TableMetaEntity.ColumnMetaData> columnMetaDataList) {
      String[] res = new String[serializers.length];
      StringConverter stringConverter = new StringConverter();

      for(int i = 0; i < serializers.length; ++i) {
         res[i] = stringConverter.convert(serializers[i], ((TableMetaEntity.ColumnMetaData)columnMetaDataList.get(i)).getType());
      }

      return res;
   }
}
