package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.PeopleRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelJob;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPeople;
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
public class PeopleServiceImpl implements PeopleService {
	
    @Autowired
    private PeopleRepository peoplerepository;

	@Override
	public Page<TeejoIntellicorriModelPeople> findAll(Pageable pageable, Object[] args) {
	    return peoplerepository.findAll(pageable);

	}
    
	@Override
	public TeejoIntellicorriModelPeople findById(String id) {
		return peoplerepository.findById(id).get();
	}

	@Override
	public void deleteById(String id) {
		peoplerepository.deleteById(id);;
	}

	@Override
	public void deleteAll() {
		peoplerepository.deleteAll();;
	}

	@Override
	public Iterable<TeejoIntellicorriModelPeople> findAll() {
		return peoplerepository.findAll();
	}

	@Override
	public TeejoIntellicorriModelPeople save(TeejoIntellicorriModelPeople TeejoIntellicorriModelPeople) {
		return peoplerepository.save(TeejoIntellicorriModelPeople);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelPeople> crawlinglist = new ArrayList<TeejoIntellicorriModelPeople>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelPeople crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelPeople.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelPeople crawling:crawlinglist){
			peoplerepository.save(crawling);
			i++;
		}
		return i;
	}
	@Override
	public Page<TeejoIntellicorriModelPeople> findByKeyword(String keyword, Pageable pageable) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder qblinename = QueryBuilders.matchPhraseQuery("linename", keyword);
        QueryBuilder qbarea = QueryBuilders.matchPhraseQuery("area", keyword);
        QueryBuilder qburl = QueryBuilders.matchPhraseQuery("name", keyword);
        QueryBuilder qbdatasource = QueryBuilders.matchPhraseQuery("datasource", keyword);
        boolQueryBuilder.should(qbdatasource);
        boolQueryBuilder.should(qblinename);
        boolQueryBuilder.should(qbarea);
        boolQueryBuilder.should(qburl);
        Page<TeejoIntellicorriModelPeople> qball  = peoplerepository.search(boolQueryBuilder,pageable);
        
        return qball;
	}

}
