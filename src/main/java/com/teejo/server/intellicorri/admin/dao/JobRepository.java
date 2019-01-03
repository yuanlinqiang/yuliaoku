package com.teejo.server.intellicorri.admin.dao;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelJob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface JobRepository extends ElasticsearchRepository<TeejoIntellicorriModelJob,String> {



}
