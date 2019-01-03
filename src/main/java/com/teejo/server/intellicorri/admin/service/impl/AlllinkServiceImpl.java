package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.AddressRepository;
import com.teejo.server.intellicorri.admin.dao.AlllinkRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.service.AlllinkService;
import com.teejo.server.intellicorri.admin.service.CrawlingService;
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
public class AlllinkServiceImpl implements AlllinkService {
	
    @Autowired
    private AlllinkRepository alllinkrepository;

	@Override
	public Page<TeejoIntellicorriModelAlllink> findAll(Pageable pageable, Object[] args) {
	    return alllinkrepository.findAll(pageable);

	}
    
	@Override
	public TeejoIntellicorriModelAlllink findById(String id) {
		return alllinkrepository.findById(id).get();
	}

	@Override
	public void deleteById(String id) {
		alllinkrepository.deleteById(id);;
	}

	@Override
	public void deleteAll() {
		alllinkrepository.deleteAll();;
	}


	@Override
	public TeejoIntellicorriModelAlllink save(TeejoIntellicorriModelAlllink TeejoIntellicorriModelAlllink) {
		return alllinkrepository.save(TeejoIntellicorriModelAlllink);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelAlllink> crawlinglist = new ArrayList<TeejoIntellicorriModelAlllink>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelAlllink crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelAlllink.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelAlllink crawling:crawlinglist){
			alllinkrepository.save(crawling);
			i++;
		}
		return i;
	}

	@Override
	public List<TeejoIntellicorriModelAlllink> findAllUrl() {
		return alllinkrepository.findAllUrl();
	}
	@Override
	public Iterable<TeejoIntellicorriModelAlllink> findUrl() {
		return    alllinkrepository.findAll();
	}

	@Override
	public Page<TeejoIntellicorriModelAlllink> findByKeyword(String keyword, Pageable pageable) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder qblinename = QueryBuilders.matchPhraseQuery("linename", keyword);
        QueryBuilder qbarea = QueryBuilders.matchPhraseQuery("area", keyword);
        QueryBuilder qbname = QueryBuilders.matchPhraseQuery("name", keyword);
        QueryBuilder qburl = QueryBuilders.matchPhraseQuery("url", keyword);
        
        boolQueryBuilder.should(qblinename);
        boolQueryBuilder.should(qbarea);
        boolQueryBuilder.should(qburl);
        boolQueryBuilder.should(qbname);
        Page<TeejoIntellicorriModelAlllink> qball  = alllinkrepository.search(boolQueryBuilder,pageable);
        return qball;
	}
}
