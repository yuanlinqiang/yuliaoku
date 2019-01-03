package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.AddressRepository;
import com.teejo.server.intellicorri.admin.dao.JobRepository;
import com.teejo.server.intellicorri.admin.dao.JobRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelJob;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelJob;
import com.teejo.server.intellicorri.admin.service.JobService;
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
public class JobServiceImpl implements JobService {
	
    @Autowired
    private JobRepository JobRepository;

	@Override
	public Page<TeejoIntellicorriModelJob> findAll(Pageable pageable, Object[] args) {
	    return JobRepository.findAll(pageable);

	}
    
	@Override
	public TeejoIntellicorriModelJob findById(String id) {
		return JobRepository.findById(id).get();
	}

	@Override
	public void deleteById(String id) {
		JobRepository.deleteById(id);;
	}

	@Override
	public void deleteAll() {
		JobRepository.deleteAll();;
	}


	@Override
	public TeejoIntellicorriModelJob save(TeejoIntellicorriModelJob TeejoIntellicorriModelJob) {
		return JobRepository.save(TeejoIntellicorriModelJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelJob> crawlinglist = new ArrayList<TeejoIntellicorriModelJob>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelJob crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelJob.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelJob crawling:crawlinglist){
			JobRepository.save(crawling);
			i++;
		}
		return i;
	}

	@Override
	public Iterable<TeejoIntellicorriModelJob> findAll() {
		return JobRepository.findAll();
	}

	@Override
	public Page<TeejoIntellicorriModelJob> findByKeyword(String keyword, Pageable pageable) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder qblinename = QueryBuilders.matchPhraseQuery("linename", keyword);
        QueryBuilder qbarea = QueryBuilders.matchPhraseQuery("area", keyword);
        QueryBuilder qburl = QueryBuilders.matchPhraseQuery("name", keyword);
        QueryBuilder qbdatasource = QueryBuilders.matchPhraseQuery("datasource", keyword);
        boolQueryBuilder.should(qbdatasource);
        boolQueryBuilder.should(qblinename);
        boolQueryBuilder.should(qbarea);
        boolQueryBuilder.should(qburl);
        Page<TeejoIntellicorriModelJob> qball  = JobRepository.search(boolQueryBuilder,pageable);
        
        return qball;
	}
}
