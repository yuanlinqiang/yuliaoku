package com.teejo.server.intellicorri.admin.common.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

public class NlpUtils {

    /**
     * 对文章进行分词，过滤之后只要人名并去重复
     * @param str
     * @return
     */
    public static List<Term> CNP(String str){
        //HanLP人名识别
//        Segment segment = HanLP.newSegment().enableNameRecognize(true);
//        List<Term> termList = segment.seg(str);

        //仅使用自定义分词
        List<Term> termList = HanLP.segment(str);
        List<Term> resTerms = new ArrayList<>();
        for (Term term:termList) {
            if(term.nature.startsWith("zzmg") || term.nature.startsWith("pxcw")){
                resTerms.add(term);
            }
        }
        List<Term> terms = notRepeat(resTerms);
        return terms;
    }

    /**
     * List<Term>去重操作
     */
    public static List<Term> notRepeat(List<Term> oldTerms){
        HashMap<String,Term> map = new HashMap<>();
        for (Term term:oldTerms) {
            map.put(term.word,term);
        }
        oldTerms.clear();
        for (Map.Entry entry:map.entrySet()) {
            Term value = (Term)entry.getValue();
            oldTerms.add(value);
        }
        return oldTerms;
    }


}
