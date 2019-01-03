package com.teejo.server.intellicorri.admin.service;

import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelAddress;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelCrawling;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelPeople;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AddressService {

	public TeejoIntellicorriModelAddress findById(String id);//根据主键获取对象

	public Page<TeejoIntellicorriModelAddress> findAll(Pageable pageable, Object[] args);//获取分页信息

	public void deleteById(String id);//获取分页信息

	public void deleteAll(); //获取分页信息

	public TeejoIntellicorriModelAddress save(TeejoIntellicorriModelAddress people);//保存数据

	public int saveFromList(List<Map<String, String>> list) throws Exception;//导入

	Iterable<TeejoIntellicorriModelAddress> findAll();

	Page<TeejoIntellicorriModelAddress> findByKeyword(String keywords, Pageable pageable);
	
}