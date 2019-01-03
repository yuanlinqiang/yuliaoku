package com.teejo.server.intellicorri.admin.common.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.seg.common.Term;
import com.teejo.server.intellicorri.admin.entity.TeejoIntellicorriModelWords;
import com.teejo.server.intellicorri.admin.service.WordsService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyCrawler extends WebCrawler {

    @Autowired
    WordsService wordsservice;
    /**
     * 正则表达式匹配指定的后缀文件
     */

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");
    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息 第二个参数url封装了当前爬取的页面url信息
     * 在这个例子中，我们指定爬虫忽略具有css，js，git，...扩展名的url，只接受以“http://www.ics.uci.edu/”开头的url。
     * 在这种情况下，我们不需要referringPage参数来做出决定。
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) { //这个方法决定了要抓取的URL及其内容。
        String href = url.getURL().toLowerCase();    //https://www.sina.com.cn/
        return !FILTERS.matcher(href).matches()  && href.startsWith("http://www.tcmap.com.cn"); //只接受以“XXXXX”开头的url
    }

    /**
     * 当一个页面被提取并准备好被你的程序处理时，这个函数被调用。
     */
    @Override
    public void visit(Page page) {  //当URL下载完成会调用这个方法。使用jsoup解析HTML，可以采用jQuery选择器的语法。
        Set<String> word = new HashSet<String>();
        String url = page.getWebURL().getURL(); // 获取url

        List<Term> termList = new ArrayList<Term>();
        String regex = "\\s*|\t|\r|\n";
        Pattern pattern = Pattern.compile(regex);
        if (page.getParseData() instanceof HtmlParseData) {  // 判断是否是html数据
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); //强制类型转换，获取html数据对象
            String text = htmlParseData.getText(); //获取页面纯文本（无html标签）
            Matcher m = pattern.matcher(text);//去除字符串中的空格,回车,换行符,制表符
            String dest = m.replaceAll("").replaceAll("[a-zA-Z]", "").trim().replaceAll("\\p{Punct}", "").replaceAll("\\d+", "");    //去掉所有英文,标点符号，数字;
            System.out.println("获取的纯文本信息+++++++++" + dest);
            termList = HanLP.segment(dest);

            TeejoIntellicorriModelWords teejointellicorrimodelwords =  new TeejoIntellicorriModelWords();
            teejointellicorrimodelwords.setWordsnature("中国");
            for (Term words : termList) {
                if ((words.nature).startsWith("ns") && word.contains(words.word) == false) {
                    word.add(words.word);
                    System.out.println("生成的分词+++++" + words.word);
                    teejointellicorrimodelwords.setWordsname(words.word);
                    wordsservice.save(teejointellicorrimodelwords);
                }

            }
        }
    }

}
