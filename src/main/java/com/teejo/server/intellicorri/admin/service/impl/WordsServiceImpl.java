package com.teejo.server.intellicorri.admin.service.impl;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.WordsRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelWords;
import com.teejo.server.intellicorri.admin.service.AlllinkService;
import com.teejo.server.intellicorri.admin.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WordsServiceImpl implements WordsService {
	
    @Autowired
    private WordsRepository wordsrepository;

	@Override
	public Page<TeejoIntellicorriModelWords> findAll(Pageable pageable, Object[] args) {
	    return wordsrepository.findAll(pageable);

	}
    
	@Override
	public TeejoIntellicorriModelWords findById(String id) {
		return wordsrepository.findById(id).get();
	}

	@Override
	public void deleteById(String id) {
		wordsrepository.deleteById(id);;
	}

	@Override
	public void deleteAll() {
		wordsrepository.deleteAll();;
	}


	@Override
	public TeejoIntellicorriModelWords save(TeejoIntellicorriModelWords TeejoIntellicorriModelWords) {
		return wordsrepository.save(TeejoIntellicorriModelWords);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelWords> crawlinglist = new ArrayList<TeejoIntellicorriModelWords>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelWords crawling = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelWords.class);
			crawlinglist.add(crawling);
		}
		for(TeejoIntellicorriModelWords crawling:crawlinglist){
			wordsrepository.save(crawling);
			i++;
		}
		return i;
	}


	@Override
	public Iterable<TeejoIntellicorriModelWords> findUrl() {
		return    wordsrepository.findAll();
	}


}
