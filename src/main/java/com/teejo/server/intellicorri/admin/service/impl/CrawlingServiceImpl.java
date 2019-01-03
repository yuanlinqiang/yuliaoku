package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.controller.CrawlingController;
import com.teejo.server.intellicorri.admin.dao.CrawlingRepository;
import com.teejo.server.intellicorri.admin.dao.PersonRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelCrawling;
import org.elasticsearch.client.transport.TransportClient;
import com.teejo.server.intellicorri.admin.service.CrawlingService;
import com.teejo.server.intellicorri.admin.service.PersonService;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.List;
import java.util.Map;

@Service
public class CrawlingServiceImpl implements CrawlingService {


    private TransportClient client;
	
    @Autowired
    private CrawlingRepository crawlingrepository;

	@Override
	public Page<TeejoIntellicorriModelCrawling> findAll(Pageable pageable, Object[] args) {
	    return crawlingrepository.findAll(pageable);

	}
    
	@Override
	public TeejoIntellicorriModelCrawling findById(String id) {
		return crawlingrepository.findById(id).get();
	}

	@Override
	public void deleteById(String id) {
		crawlingrepository.deleteById(id);;
	}

    @Override
    public void deleteAll() {
        crawlingrepository.deleteAll();
    }

    @Override
	public TeejoIntellicorriModelCrawling save(TeejoIntellicorriModelCrawling teejointellicorrimodelcrawling) {
		return crawlingrepository.save(teejointellicorrimodelcrawling);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelCrawling> crawlinglist = new ArrayList<TeejoIntellicorriModelCrawling>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelCrawling crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelCrawling.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelCrawling crawling:crawlinglist){
			crawlingrepository.save(crawling);
			i++;
		}
		return i;
	}

	@Override
	public List<TeejoIntellicorriModelCrawling> findAllUrl() {


	    //  crawlingrepository.findAllById();
		return crawlingrepository.findAllUrl();

	}
	@Override
	public Iterable<TeejoIntellicorriModelCrawling> findUrl() {
		return    crawlingrepository.findAll();
	}


    @Override
    public Iterable<TeejoIntellicorriModelCrawling> findAll() {
        return crawlingrepository.findAll();
    }

    @Override
    public Page<TeejoIntellicorriModelCrawling> findByKeyword(String keyword, Pageable pageable) {

//        WildcardQueryBuilder test = QueryBuilders.wildcardQuery("linename", "*"+keyword+"*");
//        QueryBuilder test = QueryBuilders.multiMatchQuery(keyword,"linename", "name","area");
//        QueryBuilder test = QueryBuilders.multiMatchQuery("中国","linename", "name","area");
//        QueryBuilder test = QueryBuilders.matchQuery("linename", "校园专栏");

    	//------------------------------------------------------------------
//        QueryBuilder linename = QueryBuilders.matchQuery("linename", keyword);
//        QueryBuilder name = QueryBuilders.matchQuery("name", keyword);
//        QueryBuilder area = QueryBuilders.matchQuery("area", keyword);
//        QueryBuilder url = QueryBuilders.matchQuery("url", keyword);
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.should(linename);
//        boolQueryBuilder.should(name);
//        boolQueryBuilder.should(area);
//        boolQueryBuilder.should(url);
//        Page<TeejoIntellicorriModelCrawling> crawling  = crawlingrepository.search(boolQueryBuilder,pageable);
//        return crawling;
        //-------------------------------------------------------------------
        
        
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder qbname = QueryBuilders.matchPhraseQuery("name", keyword);
        QueryBuilder qblinename = QueryBuilders.matchPhraseQuery("linename", keyword);
        QueryBuilder qbarea = QueryBuilders.matchPhraseQuery("area", keyword);
        QueryBuilder qburl = QueryBuilders.matchPhraseQuery("url", keyword);
        
        boolQueryBuilder.should(qbname);
        boolQueryBuilder.should(qblinename);
        boolQueryBuilder.should(qbarea);
        boolQueryBuilder.should(qburl);
        
        Page<TeejoIntellicorriModelCrawling> qball  = crawlingrepository.search(boolQueryBuilder,pageable);
        
        return qball;

    }

    private void searchFunction(QueryBuilder queryBuilder) {
        SearchResponse response = client.prepareSearch("twitter")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setScroll(new TimeValue(60000))
                .setQuery(queryBuilder)
                .setSize(100).execute().actionGet();

        while(true) {
            response = client.prepareSearchScroll(response.getScrollId())
                    .setScroll(new TimeValue(60000)).execute().actionGet();
            for (SearchHit hit : response.getHits()) {
                Iterator<Entry<String, Object>> iterator = hit.getSource().entrySet().iterator();
                while(iterator.hasNext()) {
                    Entry<String, Object> next = iterator.next();
                    System.out.println(next.getKey() + ": " + next.getValue());
                    if(response.getHits().hits().length == 0) {
                        break;
                    }
                }
            }
            break;
        }

    }



}
   // public Page<TeejoIntellicorriModelCrawling> findByKeyword(String keywords, Pageable pageable) {
//        // WildcardQueryBuilder areaBuilderss = QueryBuilders.wildcardQuery("area", "*" +keywords +"*");
//        WildcardQueryBuilder areaBuilderss = QueryBuilders.wildcardQuery("area", "*河南*");
//        WildcardQueryBuilder linenameBuilderss = QueryBuilders.wildcardQuery("linename", "*" +keywords +"*");
//        WildcardQueryBuilder nameBuilderss = QueryBuilders.wildcardQuery("name", "*" +keywords +"*");
//        WildcardQueryBuilder urlBuilderss = QueryBuilders.wildcardQuery("url", "*" +keywords +"*");
////        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
////        boolQueryBuilder.should(areaBuilderss);
////        boolQueryBuilder.should(linenameBuilderss);
////        boolQueryBuilder.should(nameBuilderss);
////        boolQueryBuilder.should(urlBuilderss);
//
//
//        Page<TeejoIntellicorriModelCrawling> data1111  = crawlingrepository.search(areaBuilderss,pageable);
//        Page<TeejoIntellicorriModelCrawling> data2222  = crawlingrepository.search(linenameBuilderss,pageable);
//        Page<TeejoIntellicorriModelCrawling> data3333  = crawlingrepository.search(nameBuilderss,pageable);
//        Page<TeejoIntellicorriModelCrawling> data4444  = crawlingrepository.search(urlBuilderss,pageable);
//
//
//
//        //   Page<TeejoIntellicorriModelCrawling> find2222  = crawlingrepository.search(boolQueryBuilder,pageable);
//        Page<TeejoIntellicorriModelCrawling> find3333  = crawlingrepository.search(urlBuilderss,pageable);
//
//
//        return find3333;
//    }