package com.teejo.server.intellicorri.admin.controller;

import com.teejo.server.intellicorri.admin.common.CommonController;
import com.teejo.server.intellicorri.admin.common.utils.MyCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import io.swagger.annotations.Api;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address111")
@Api(value="地点名称",tags={"地点名称-增删改查;导入导出"})
@EnableScheduling
public class Crawler4jController extends CommonController {

    @RequestMapping(value = "/getpagedata", method = { RequestMethod.POST })
    public void  getpagedata() throws  Exception{
        String crawlStorageFolder = "E:/Crawling";// 定义爬虫数据存储位置
        int numberOfCrawlers = 7;// 定义了7个爬虫，也就是7个线程

        CrawlConfig config = new CrawlConfig();// 定义爬虫配置
        config.setCrawlStorageFolder(crawlStorageFolder);// 设置爬虫文件存储位置
        //config.setResumableCrawling(true); //有时爬虫需要运行很长时间，但中途可能意外终止了。这种情况下，可以通过以下配置恢复停止/崩溃的爬虫：
       // config.setConnectionTimeout(500000);
        /*
         * 实例化爬虫控制器。
         */
        PageFetcher pageFetcher = new PageFetcher(config);// 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
        // 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("http://www.tcmap.com.cn");
        //controller.addSeed("http://www.tcmap.com.cn/beijing/xichengqu.html");
       controller.start(MyCrawler.class, numberOfCrawlers);
    }




	public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "E:/Crawling";// 定义爬虫数据存储位置
        int numberOfCrawlers = 7;// 定义了7个爬虫，也就是7个线程
        CrawlConfig config = new CrawlConfig();// 定义爬虫配置
        config.setCrawlStorageFolder(crawlStorageFolder);// 设置爬虫文件存储位置
        //config.setResumableCrawling(true); //有时爬虫需要运行很长时间，但中途可能意外终止了。这种情况下，可以通过以下配置恢复停止/崩溃的爬虫：
        /*
         * 实例化爬虫控制器。
         */
		PageFetcher pageFetcher = new PageFetcher(config);// 实例化页面获取器
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
        // 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		controller.addSeed("http://www.tcmap.com.cn");
		//controller.addSeed("http://www.tcmap.com.cn/beijing/xichengqu.html");
		controller.start(MyCrawler.class, numberOfCrawlers);

	}

}