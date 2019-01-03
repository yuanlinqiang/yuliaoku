package com.teejo.server.intellicorri.admin.dao;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPeople;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface AddressRepository extends ElasticsearchRepository<TeejoIntellicorriModelAddress,String> {



}
