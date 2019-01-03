package com.teejo.server.intellicorri.admin.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPerson;

public interface PersonService {
	
	public TeejoIntellicorriModelPerson findById(String personid);//根据主键获取对象
	
	public Page<TeejoIntellicorriModelPerson> findAll(Pageable pageable,Object[] args);//获取分页信息
	
	public void deleteById(String personid);//获取分页信息
	
	public TeejoIntellicorriModelPerson save(TeejoIntellicorriModelPerson person);//保存数据
	
	public int saveFromList(List<Map<String,String>> list) throws Exception;//导入

	TeejoIntellicorriModelPerson  findPersonByName(String   name);
}