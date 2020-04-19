package com.sunxn.news.item.mapper;

import com.sunxn.news.pojo.CarouselNews;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * @description:
 * @data: 2020/4/18 22:27
 * @author: xiaoNan
 */
public interface CarouselNewsMapper extends Mapper<CarouselNews>, DeleteByIdsMapper<CarouselNews> {
}
