package com.teejo.server.intellicorri.admin.controller;

import com.teejo.server.intellicorri.admin.common.CommonController;
import com.teejo.server.intellicorri.admin.common.beans.CommonResult;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelWords;
import com.teejo.server.intellicorri.admin.service.WordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/words")
@Api(value="子链接地址",tags={"子链接地址-增删改查;导入导出"})
@EnableScheduling
public class WordsController extends CommonController {

	@Autowired
	WordsService wordsservice;

	@ApiOperation(value = "获取分页数据" ,notes = "获取分页数据" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageable" ,value = "分页" , required = false, dataType = "Pageable")
			,@ApiImplicitParam(name = "keywords" ,value = "搜索关键字" , required = false, dataType = "String")
	})
	@RequestMapping(value = "/pagedata", method = { RequestMethod.GET  })
	public Page<TeejoIntellicorriModelWords> pagedata(Pageable pageable, String keywords){
		return wordsservice.findAll(pageable,new Object[]{keywords});
	}

	@ApiOperation(value = "获取单条数据对象" ,notes = "获取单条数据对象")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query",name = "id" ,value = "地址ID" , required = true, dataType = "String")
	})
	@RequestMapping(value = "/singledata" ,method = { RequestMethod.GET })
	public TeejoIntellicorriModelWords singledata(String id){
		return wordsservice.findById(id);
	}


	@ApiOperation(value = "删除地址", notes = "删除地址" )
	@ApiImplicitParams({ @ApiImplicitParam(name = "urlids", value = "地址ID", required = true, dataType = "String")
	})
	@RequestMapping(value = "/delete" ,method = { RequestMethod.DELETE})
	public CommonResult delete(String urlids) {
		try {
//			String[] id_array = urlids.split(",");
//			for(String id:id_array){
//				wordsservice.deleteById(id);
//			}
			wordsservice.deleteAll();
			cr = new CommonResult(true,0,null,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cr;
	}

	@ApiOperation(value = "保存新闻地址", notes = "保存新闻地址,id列为空则为新增,不为空则为修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "TeejoIntellicorriModelWords",value = "数据",required = false,dataType = "TeejoIntellicorriModelWords")
			,@ApiImplicitParam(name = "file",value = "文件",required = false,dataType = "MultipartFile")
	})
	@RequestMapping(value = "/save" ,method = { RequestMethod.POST })
	public CommonResult save(TeejoIntellicorriModelWords TeejoIntellicorriModelWords,MultipartFile file) {
		try {
			wordsservice.save(TeejoIntellicorriModelWords);
			cr = new CommonResult(true,0,null,"保存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return   cr;
	}

	@ApiOperation(value = "导出数据", notes = "导出数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "keywords" ,value = "搜索关键字" , required = false, dataType = "String")
	})
	@RequestMapping(value = "/expexcel", method = { RequestMethod.GET  })
	public ResponseEntity<byte[]> expexcel( Pageable pageable,String keywords) {
		ResponseEntity<byte[]> entity = null;
		String[] titleNameArray = {"词性","词汇"};
		String[] fieldNameArray = {"wordsnature","wordsname"};
		try {
			//根据条件获取数据
			List<TeejoIntellicorriModelWords> data = wordsservice.findAll(pageable,new Object[]{keywords}).getContent();
			//数据转换成流并导出
			InputStream is = super.exportExcelContent(data,titleNameArray,fieldNameArray);
			byte[] body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			String exportFilename = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + ".xls";
			headers.add("Content-Disposition", "attchement;filename=" + exportFilename);
			HttpStatus statusCode = HttpStatus.OK;
			entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	@ApiOperation(value = "导入数据", notes = "导入数据")
	@RequestMapping(value = "/impexcel", method = { RequestMethod.POST })
	public CommonResult impexcel(@RequestParam("file") MultipartFile file) {
		int imp_num = 0;
		//如果文件不为空，写入上传路径
		try {
			if(!file.isEmpty()) {
				String[] fieldNameArray = {"wordsnature","wordsname"};
				List<Map<String,String>> list = super.getExcelContent(file, fieldNameArray);
				imp_num =  wordsservice.saveFromList(list);
				cr = new CommonResult(true,0,null,"导入成功，导入数据："+imp_num+"条！");
			} else {
				cr = new CommonResult(false,0,null,"文件上传失败！");
			}
		} catch (Exception e) {
			cr = new CommonResult(false,0,null,"导入失败,请确认Excel内容是否正确。</br>错误信息："+super.getPointOfException(e.getMessage()));
		}
		return  cr;
	}



	public static void main(String[] args) throws Exception {

	}

}