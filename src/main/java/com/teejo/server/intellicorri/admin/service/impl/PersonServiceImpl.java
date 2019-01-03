package com.teejo.server.intellicorri.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.teejo.server.intellicorri.admin.common.utils.GsonUtil;
import com.teejo.server.intellicorri.admin.dao.PersonRepository;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPerson;
import com.teejo.server.intellicorri.admin.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	
    @Autowired
    private PersonRepository personRepository;

	@Override
	public Page<TeejoIntellicorriModelPerson> findAll(Pageable pageable,Object[] args) {
	    return personRepository.findAll(pageable);
	}
    
	@Override
	public TeejoIntellicorriModelPerson findById(String personid) {
		return personRepository.findById(personid).get();
	}

	@Override
	//@CacheEvict(value="TeejoCache", allEntries=true)    开启redis的时候开启
	public void deleteById(String personid) {
		personRepository.deleteById(personid);;
	}

	@Override
	public TeejoIntellicorriModelPerson save(TeejoIntellicorriModelPerson person) {
		return personRepository.save(person);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveFromList(List<Map<String,String>> list) throws Exception {
		int i=0;
		List<TeejoIntellicorriModelPerson> personlist = new ArrayList<TeejoIntellicorriModelPerson>();
		for(Map<String,String> map:list){
			TeejoIntellicorriModelPerson person = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(map),TeejoIntellicorriModelPerson.class);
			personlist.add(person);
		}
		for(TeejoIntellicorriModelPerson person:personlist){
			personRepository.save(person);
			i++;
		}
		return i;
	}

	@Override
	public TeejoIntellicorriModelPerson findPersonByName(String name) {
		return personRepository.findPersonByName(name);
	}
}
