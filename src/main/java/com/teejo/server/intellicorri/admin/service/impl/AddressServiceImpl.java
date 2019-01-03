package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.AddressRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelCrawling;
import com.teejo.server.intellicorri.admin.service.AddressService;
import com.teejo.server.intellicorri.admin.service.PeopleService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository AddressRepository;

	@Override
	public Page<TeejoIntellicorriModelAddress> findAll(Pageable pageable, Object[] args) {
	    return AddressRepository.findAll(pageable);

	}

	@Override
	public TeejoIntellicorriModelAddress findById(String id) {
		return AddressRepository.findById(id).get();
	}

	@Override
	public Iterable<TeejoIntellicorriModelAddress> findAll() {
		return AddressRepository.findAll();
	}

	@Override
	public void deleteById(String id) {
		AddressRepository.deleteById(id);;
	}

	@Override
	public void deleteAll() {
		AddressRepository.deleteAll();;
	}


	@Override
	public TeejoIntellicorriModelAddress save(TeejoIntellicorriModelAddress TeejoIntellicorriModelAddress) {
		return AddressRepository.save(TeejoIntellicorriModelAddress);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelAddress> crawlinglist = new ArrayList<TeejoIntellicorriModelAddress>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelAddress crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelAddress.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelAddress crawling:crawlinglist){
			AddressRepository.save(crawling);
			i++;
		}
		return i;
	}
	@Override
	public Page<TeejoIntellicorriModelAddress> findByKeyword(String keyword, Pageable pageable) {
		
		    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	        QueryBuilder qblinename = QueryBuilders.matchPhraseQuery("linename", keyword);
	        QueryBuilder qbarea = QueryBuilders.matchPhraseQuery("area", keyword);
	        QueryBuilder qburl = QueryBuilders.matchPhraseQuery("name", keyword);
	        QueryBuilder qbdatasource = QueryBuilders.matchPhraseQuery("datasource", keyword);
	        boolQueryBuilder.should(qbdatasource);
	        
	        boolQueryBuilder.should(qblinename);
	        boolQueryBuilder.should(qbarea);
	        boolQueryBuilder.should(qburl);
	        
	        Page<TeejoIntellicorriModelAddress> qball  = AddressRepository.search(boolQueryBuilder,pageable);
	        
	        return qball;
	}

}
