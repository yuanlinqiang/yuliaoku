package com.teejo.server.intellicorri.admin.service;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAlllink;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPeople;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PeopleService {

	public TeejoIntellicorriModelPeople findById(String id);//根据主键获取对象

	public Page<TeejoIntellicorriModelPeople> findAll(Pageable pageable, Object[] args);//获取分页信息

	public void deleteById(String id);//获取分页信息

	public void deleteAll(); //获取分页信息

	public TeejoIntellicorriModelPeople save(TeejoIntellicorriModelPeople people);//保存数据

	public int saveFromList(List<Map<String, String>> list) throws Exception;//导入

	Iterable<TeejoIntellicorriModelPeople> findAll();

	Page<TeejoIntellicorriModelPeople> findByKeyword(String keywords, Pageable pageable);
	
}