package com.teejo.server.intellicorri.admin.dao;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelCrawling;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Title: PersonRepository
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/8/16 9:57
 */
public interface AlllinkRepository extends ElasticsearchRepository<TeejoIntellicorriModelAlllink,String> {

    @Query(value="select * from  TeejoIntellicorriModelAlllink" )
    List<TeejoIntellicorriModelAlllink> findAllUrl();

}
