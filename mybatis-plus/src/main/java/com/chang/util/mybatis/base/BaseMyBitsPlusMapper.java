package com.chang.util.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseMyBitsPlusMapper<T> extends MppBaseMapper<T> {
   List<T> selectStream(@Param("ew") Wrapper<T> wrapper, @Param("start") Long start, @Param("size") Long size);

   default List<T> selectAll() {
      return this.selectList(Wrappers.emptyWrapper());
   }

   default QueryWrapper<T> getQueryWrapper() {
      return new QueryWrapper();
   }

   default UpdateWrapper<T> getUpdateWrapper() {
      return new UpdateWrapper();
   }

   default LambdaQueryWrapper<T> getLambdaQueryWrapper() {
      return Wrappers.lambdaQuery();
   }

   default LambdaUpdateWrapper<T> getLambdaUpdateWrapper() {
      return Wrappers.lambdaUpdate();
   }
}
