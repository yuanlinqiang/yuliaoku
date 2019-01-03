package com.teejo.server.intellicorri.admin.dao;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelCrawling;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.Repository;

import java.awt.print.Book;
import java.util.List;

/**
 * @Title: PersonRepository
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/8/16 9:57
 */
public interface CrawlingRepository extends ElasticsearchRepository<TeejoIntellicorriModelCrawling,String> ,Repository<TeejoIntellicorriModelCrawling,String> {

    @Query(value="select * from  data2" )
    List<TeejoIntellicorriModelCrawling> findAllUrl();

    @Query(value="select Max(id)  from  TeejoIntellicorriModelCrawling " )
    public  Integer  findMaxSn();

//    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"name\" : \"?0\"}}}}")
//    Page<TeejoIntellicorriModelCrawling> findByName(String name, Pageable pageable);



}
