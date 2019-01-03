package com.teejo.server.intellicorri.admin.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.teejo.server.intellicorri.admin.common.CommonController;
import com.teejo.server.intellicorri.admin.common.beans.CommonResult;
import com.teejo.server.intellicorri.admin.dao.AlllinkRepository;
import com.teejo.server.intellicorri.admin.dao.CrawlingRepository;
import com.teejo.server.intellicorri.admin.entity.*;
import com.teejo.server.intellicorri.admin.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.IterableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ScheduledExecutorTask;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/crawling")
@Api(value="数据爬取接口",tags={"crawling(数据爬取)-增删改查;导入导出"})
@EnableScheduling
public class CrawlingController extends CommonController {
	
    @Autowired
	CrawlingService crawlingservice;
    @Autowired
    AlllinkService alllinkservice;
    @Autowired
    WordsService wordsservice;
    @Autowired
    PeopleService peopleservice;
    @Autowired
    AddressService addressservice;
    @Autowired
    JobService jobservice;
    @Autowired
    CrawlingRepository crawlingrepository;
    @Autowired
    AlllinkRepository alllinkrepository;


	@ApiOperation(value = "获取分页数据" ,notes = "获取分页数据" )
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageable" ,value = "分页" , required = false, dataType = "Pageable")
		,@ApiImplicitParam(name = "keywords" ,value = "搜索关键字" , required = false, dataType = "String")
	})
    @RequestMapping(value = "/pagedata", method = { RequestMethod.GET  })
    public Page<TeejoIntellicorriModelCrawling> pagedata(Pageable pageable, String keywords){
        Page<TeejoIntellicorriModelCrawling> pageda  = null ;
        if(!"".equals(keywords)){
           pageda =crawlingservice.findByKeyword(keywords,pageable);
        }else{
            pageda = crawlingservice.findAll(pageable,new Object[]{keywords});
        }
         return pageda;
    }

	@ApiOperation(value = "获取单条数据对象" ,notes = "获取单条数据对象")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query",name = "id" ,value = "地址ID" , required = true, dataType = "String")
	})
	@RequestMapping(value = "/singledata" ,method = { RequestMethod.GET })
	public TeejoIntellicorriModelCrawling singledata(String id){
		return crawlingservice.findById(id);
	}
	
	
	@ApiOperation(value = "删除地址", notes = "删除地址" )
	@ApiImplicitParams({ @ApiImplicitParam(name = "urlids", value = "地址ID", required = true, dataType = "String")
	})
	@RequestMapping(value = "/delete" ,method = { RequestMethod.DELETE})
	public CommonResult delete(String urlids) {
		try {
			String[] id_array = urlids.split(",");
			for(String id:id_array){
				crawlingservice.deleteById(id);
			}
			cr = new CommonResult(true,0,null,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cr;
	}

    @ApiOperation(value = "删除所有新闻网站", notes = "删除所有新闻网站" )
    @RequestMapping(value = "/deleteAll" ,method = { RequestMethod.DELETE})
    public CommonResult deleteAll() {
        try {
            crawlingservice.deleteAll();
            cr = new CommonResult(true,0,null,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr;
    }


	@ApiOperation(value = "保存新闻地址", notes = "保存新闻地址,id列为空则为新增,不为空则为修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "TeejoIntellicorriModelCrawling",value = "数据",required = false,dataType = "TeejoIntellicorriModelCrawling")
		,@ApiImplicitParam(name = "file",value = "文件",required = false,dataType = "MultipartFile")
	})
	@RequestMapping(value = "/save" ,method = { RequestMethod.POST })
	public CommonResult save(TeejoIntellicorriModelCrawling teejointellicorrimodelcrawling,MultipartFile file) {
		try {
			crawlingservice.save(teejointellicorrimodelcrawling);
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
	    String[] titleNameArray = {"序号","网站名称","栏目名称","栏目网址"};
		String[] fieldNameArray = {"sn","name","linename","url"};
		try {
			//根据条件获取数据
            List<TeejoIntellicorriModelCrawling> data = crawlingservice.findAll(pageable,new Object[]{keywords}).getContent();
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
				String[] fieldNameArray = {"sn","name","linename","url"};
				List<Map<String,String>> list = super.getExcelContent(file, fieldNameArray);
                imp_num =  crawlingservice.saveFromList(list);
				cr = new CommonResult(true,0,null,"导入成功，导入数据："+imp_num+"条！");
			} else {
				cr = new CommonResult(false,0,null,"文件上传失败！");
			}
		} catch (Exception e) {
			cr = new CommonResult(false,0,null,"导入失败,请确认Excel内容是否正确。</br>错误信息："+super.getPointOfException(e.getMessage()));
		}
		return  cr;
	}


	//凌晨两点爬取所有数据
//    @Scheduled(cron="0 0 2 * * ?")
//	public void getAll() throws Exception {
//        getAll("");
//    }


    @ApiOperation(value = "通过栏目网址爬取所有可链接地址", notes = "通过栏目网址爬取所有可链接地址")
    @RequestMapping(value = "/getAllUrlByLineName", method = {RequestMethod.POST})
    public void getAll(String urlids) throws Exception {
        URL realURL = null;
        URLConnection connection = null;
        BufferedReader br = null;
        String originalUrl, url, hostAddress, line = null;
        String regex = "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
        String baiduUrl = "https://www.baidu.com";
        Integer x = 10;
        Integer y = 0;
        Integer sns = 0;
        Set<String> allUrlData = new HashSet<String>();
        Set<TeejoIntellicorriModelAlllink> allLinkData = new HashSet<TeejoIntellicorriModelAlllink>();
        Set<String> nextUrlData = new HashSet<String>();
        Set<String> newUrlData = new HashSet<String>();
        Pattern pattern = Pattern.compile(regex);
        List<TeejoIntellicorriModelCrawling> myList = new ArrayList<TeejoIntellicorriModelCrawling>();
        Collection<String> a = new ArrayList<String>();
        if (urlids != null) {//查询选中的栏目的链接地址
            String[] id_array = urlids.split(",");
            for (String id : id_array) {
                a.add(id);
            }
            Iterator<String> iterator = a.iterator();
            Iterable<TeejoIntellicorriModelCrawling> find = crawlingrepository.findAllById(a);
            myList = IterableUtils.toList(find);
        } else {//查询所有栏目的链接地址
            Iterable<TeejoIntellicorriModelCrawling> findUrl = crawlingservice.findUrl();
            myList = IterableUtils.toList(findUrl);
        }
        TeejoIntellicorriModelAlllink alllink = null;
        for (int j = 0; j < myList.size(); j++) {
            URL hostAddressurl = new URL(myList.get(j).getUrl());
            hostAddress = hostAddressurl.getProtocol() + "://" + hostAddressurl.getHost();
            originalUrl = myList.get(j).getUrl(); //原始的URL   去重用
            url = myList.get(j).getUrl();
            newUrlData.add(myList.get(j).getUrl());
            for (int i = -1; i < x; i++) {
                try {
                    List<String> newsUrlData = new ArrayList<String>(newUrlData);
                    if (newsUrlData.size() > 1 && y == 0) {
                        sns += 1;
                        alllink = new TeejoIntellicorriModelAlllink();
                        url = newsUrlData.get(i);
                        alllink.setId(UUID.randomUUID().toString().replace("-", ""));
                        alllink.setSn(sns);
                        alllink.setArea(myList.get(j).getArea());
                        alllink.setName(myList.get(j).getName());
                        alllink.setLinename(myList.get(j).getLinename());
                        alllink.setUrl(url);
                        allLinkData.add(alllink);
                       // alllinkservice.save(alllink);   //保存连接

                    } else if (y == 1) {
                        url = baiduUrl;   //百度只能爬取一次，如果最后一次循环报错，可作为中间过度再次进入循环
                        y = 0;
                    }
                    realURL = new URL(url);
                    connection = realURL.openConnection();
                    connection.setConnectTimeout(50000000);
                    connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");// 服务器的安全设置   否则不接受Java程序作为客户端访问
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        while (matcher.find()) {
                            if (matcher.group().startsWith(hostAddress) && allUrlData.contains(matcher.group()) == false) {
                                if (matcher.group().endsWith(".html") || matcher.group().endsWith(".htm") || matcher.group().endsWith(".com") || matcher.group().endsWith(".cn") || matcher.group().endsWith(".shtml") || matcher.group().endsWith(".org")) {
                                    allUrlData.add(matcher.group());
                                }
                            }
                        }
                    }
                    System.out.println("当前循环的网址次数"+ i + "---------循环的网址的个数-------" + j);
                    if (i == newUrlData.size() - 1) {//判断循环是否完成或进入新循环
                        newUrlData.addAll(allUrlData);
                        newUrlData.removeAll(nextUrlData);
                        newUrlData.remove(originalUrl);
                        newUrlData.remove(baiduUrl);
                        if (allUrlData.size() == nextUrlData.size()) {//没有循环的时候跳出循环
                            break;
                        }
                        nextUrlData.addAll(allUrlData);
                        x = newUrlData.size();
                        i = -1;
                        System.out.println("重新生成的条数++++" + x);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();        网址链接不存在、失效、拒绝访问或者反爬墙，直接跳出此次循环
                } finally {
                    try {
                        br.close();
                        if (i == newUrlData.size() - 1) {//假如最后一个是无效链接   定义BaiduURL重走循环
                            y = 1;
                            i--;
                        }
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("当前时间" + new Timestamp(System.currentTimeMillis()));
        getDoc(allLinkData);//获取纯文本数据
    }

    //获取纯文本信息
    public Document getDoc(Set<TeejoIntellicorriModelAlllink> allLinkData) {
        Document document = null;
        String url = null;
        String regex = "\\s*|\t|\r|\n";
        Pattern pattern = Pattern.compile(regex);
        Set<String> contentSet = new HashSet<String>();
        Set<TeejoIntellicorriModelAlllink> newcontent =  new HashSet<TeejoIntellicorriModelAlllink>();
       
        List<Term> termList = new ArrayList<Term>();
        Set<String> allWords = new HashSet<String>();
        Set<String> peopleSet = new HashSet<String>();
        Set<String> addressSet = new HashSet<String>();
        Set<String> jobSet = new HashSet<String>();
        Integer addressn = 0;
        Integer jobsn = 0;
        Integer peoplesn = 0;
        TeejoIntellicorriModelPeople people = new TeejoIntellicorriModelPeople();
        TeejoIntellicorriModelAddress address = new TeejoIntellicorriModelAddress();
        TeejoIntellicorriModelJob job = new TeejoIntellicorriModelJob();

        Iterable<TeejoIntellicorriModelPeople> peopleAlreadySet = peopleservice.findAll();
        for (TeejoIntellicorriModelPeople peoples : peopleAlreadySet) { //查询已经保存的人名
            peopleSet.add(peoples.getName());
        }
        Iterable<TeejoIntellicorriModelAddress> addressAlreadySet = addressservice.findAll();
        for (TeejoIntellicorriModelAddress addresss : addressAlreadySet) {  //查询已经保存的地名
            addressSet.add(addresss.getName());
        }
        Iterable<TeejoIntellicorriModelJob> jobAlreadySet = jobservice.findAll();
        for (TeejoIntellicorriModelJob job1 : jobAlreadySet) {  //查询已经保存的职务
            jobSet.add(job1.getName());
        }
        Integer   ss  = 0;
        for (TeejoIntellicorriModelAlllink str : allLinkData) {
        	ss += 1;
            try {
                url = str.getUrl();
                boolean flag = false;
                do {
                    try {
                        document = Jsoup
                                .connect(url)
                                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                                .timeout(50000)
                                .get();
                        flag = false;
                    } catch (IOException e) {
                        // e.printStackTrace();  网址链接不存在、失效、拒绝访问或者反爬墙，直接跳出此次循环
                    }
                } while (flag);
                String htmlcontent = document.toString().replaceAll("</?[^>]+>", ""); //剔出<html>的标签
                Matcher m = pattern.matcher(htmlcontent);//去除字符串中的空格,回车,换行符,制表符
                String dest = m.replaceAll("");
                String contentText = dest.replaceAll("[a-zA-Z]", "").trim().replaceAll("\\p{Punct}", "").replaceAll("\\d+", "");    //去掉所有英文,标点符号，数字
                    termList = HanLP.segment(contentText);
                    Integer times = 0;
                    System.out.println("当前循环的条数+++" + ss + "+++总的条数+++" + allLinkData.size() );
                    for (Term words : termList) {
                        times += 1;
                        if ((words.nature).startsWith("ns") && addressSet.contains(words.word) == false) { //保存数据集合里面没有的地名
                            addressSet.add(words.word);
                            addressn += 1;
                            address.setId(UUID.randomUUID().toString().replace("-", ""));
                            address.setSn(addressn);
                            address.setName(words.word);
                            address.setArea(str.getArea());
                            address.setDatasource(str.getName());
                            address.setIslabel("否");
                            address.setLinename(str.getLinename());
                            addressservice.save(address);
                        } else if ((words.nature).startsWith("nnt") && jobSet.contains(words.word) == false) {//保存数据集合里面没有的职务
                            jobSet.add(words.word);
                            jobsn += 1;
                            job.setId(UUID.randomUUID().toString().replace("-", ""));
                            job.setSn(jobsn);
                            job.setName(words.word);
                            job.setDatasource(str.getName());
                            job.setIslabel("否");
                            job.setArea(str.getArea());
                            job.setLinename(str.getLinename());
                            jobservice.save(job);
                        } else if ((words.nature).startsWith("nr")  && peopleSet.contains(words.word) == false ) {//保存数据集合里面没有的人名
                            peopleSet.add(words.word);
                            peoplesn += 1;
                            people.setId(UUID.randomUUID().toString().replace("-", ""));
                            people.setSn(peoplesn);
                            people.setDatasource(str.getName());
                            people.setIslabel("否");
                            people.setName(words.word);
                            people.setArea(str.getArea());
                            people.setLinename(str.getLinename());
                            peopleservice.save(people);
                        }
                        if(times %5 == 0 ){
                        	System.out.println("当前次数+++" + times + "+++总的分词+++" + termList.size());	
                        }
                        
                    }
            } catch (Exception e) {
                //e.printStackTrace();        网址链接不存在、失效、拒绝访问或者反爬墙，直接跳出此次循环
            }
        }
        return document;
    }
    //调取HandNLP   获取分词
    public void hanlp(Set<TeejoIntellicorriModelAlllink> newcontent) {
        List<Term> termList = new ArrayList<Term>();
        Set<String> allWords = new HashSet<String>();
        Set<String> peopleSet = new HashSet<String>();
        Set<String> addressSet = new HashSet<String>();
        Set<String> jobSet = new HashSet<String>();
        Integer addressn = 0;
        Integer jobsn = 0;
        Integer peoplesn = 0;
        TeejoIntellicorriModelPeople people = new TeejoIntellicorriModelPeople();
        TeejoIntellicorriModelAddress address = new TeejoIntellicorriModelAddress();
        TeejoIntellicorriModelJob job = new TeejoIntellicorriModelJob();

        Iterable<TeejoIntellicorriModelPeople> peopleAlreadySet = peopleservice.findAll();
        for (TeejoIntellicorriModelPeople peoples : peopleAlreadySet) { //查询已经保存的人名
            peopleSet.add(peoples.getName());
        }
        Iterable<TeejoIntellicorriModelAddress> addressAlreadySet = addressservice.findAll();
        for (TeejoIntellicorriModelAddress addresss : addressAlreadySet) {  //查询已经保存的地名
            addressSet.add(addresss.getName());
        }
        Iterable<TeejoIntellicorriModelJob> jobAlreadySet = jobservice.findAll();
        for (TeejoIntellicorriModelJob job1 : jobAlreadySet) {  //查询已经保存的职务
            jobSet.add(job1.getName());
        }
        Integer time = 0;
        for (TeejoIntellicorriModelAlllink textcontent : newcontent) {
            termList = HanLP.segment(textcontent.getContent());
            Integer times = 0;
            time += 1;
            for (Term words : termList) {
                times += 1;
                if ((words.nature).startsWith("ns") && addressSet.contains(words.word) == false) { //保存数据集合里面没有的地名
                    addressSet.add(words.word);
                    addressn += 1;
                    address.setId(UUID.randomUUID().toString().replace("-", ""));
                    address.setSn(addressn);
                    address.setName(words.word);
                    address.setArea(textcontent.getArea());
                    address.setDatasource(textcontent.getName());
                    address.setIslabel("否");
                    address.setLinename(textcontent.getLinename());
                    addressservice.save(address);
                } else if ((words.nature).startsWith("nnt") && jobSet.contains(words.word) == false) {//保存数据集合里面没有的职务
                    jobSet.add(words.word);
                    jobsn += 1;
                    job.setId(UUID.randomUUID().toString().replace("-", ""));
                    job.setSn(jobsn);
                    job.setName(words.word);
                    job.setDatasource(textcontent.getName());
                    job.setIslabel("否");
                    job.setArea(textcontent.getArea());
                    job.setLinename(textcontent.getLinename());
                    jobservice.save(job);
                } else if ((words.nature).startsWith("nr")  && peopleSet.contains(words.word) == false ) {//保存数据集合里面没有的人名
                    peopleSet.add(words.word);
                    peoplesn += 1;
                    people.setId(UUID.randomUUID().toString().replace("-", ""));
                    people.setSn(peoplesn);
                    people.setDatasource(textcontent.getName());
                    people.setIslabel("否");
                    people.setName(words.word);
                    people.setArea(textcontent.getArea());
                    people.setLinename(textcontent.getLinename());
                    peopleservice.save(people);
                }
                System.out.println("nowtime+++" + times + "+++allnum" + termList.size()+ "+++nowNUMS" + time+ "+++allNUMS" + newcontent.size());
            }
        }
        System.out.println("数据分词结束");
    }
    @ApiOperation(value = "获取相关网站的纯文本消息", notes = "获取相关网站的纯文本消息")
    @RequestMapping(value = "/getDocText", method = {RequestMethod.POST})
    public void getDocText(String urlids) throws Exception {
        Set<TeejoIntellicorriModelAlllink> alllink = new HashSet<TeejoIntellicorriModelAlllink>();
        List<TeejoIntellicorriModelAlllink> alllinkList = new ArrayList<TeejoIntellicorriModelAlllink>();
        Collection<String> a = new ArrayList<String>();
        System.out.println("已经进入设置页面");
        if (urlids != null) {//查询选中的网址的链接地址
            String[] id_array = urlids.split(",");
            for (String id : id_array) {
                a.add(id);
            }
            Iterator<String> iterator = a.iterator();
            Iterable<TeejoIntellicorriModelAlllink> find = alllinkrepository.findAllById(a);
            alllinkList = IterableUtils.toList(find);
        } else {//查询所有网址链接
            Iterable<TeejoIntellicorriModelAlllink> findUrl = alllinkservice.findUrl();
            alllinkList = IterableUtils.toList(findUrl);
        }
        alllink = new HashSet<TeejoIntellicorriModelAlllink>(alllinkList);
        getDoc(alllink);
        System.out.println("结束结束结束");
    }





    public static void main(String[] args) throws Exception {

    }

}