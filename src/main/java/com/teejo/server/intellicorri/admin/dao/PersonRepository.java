package com.teejo.server.intellicorri.admin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPerson;

/**
 * @Title: PersonRepository
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/8/16 9:57
 */
public interface PersonRepository extends ElasticsearchRepository<TeejoIntellicorriModelPerson,String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}]}}")
    TeejoIntellicorriModelPerson findPersonByName(String name);

    TeejoIntellicorriModelPerson queryPersonById(String id);

    List<TeejoIntellicorriModelPerson> findByName(String name);

    List<TeejoIntellicorriModelPerson> findByNameAndWork(String name, String work);

    Page<TeejoIntellicorriModelPerson> findByWork(String work, Pageable pageable);

}
