package com.teejo.server.intellicorri.admin.entity;

import com.hankcs.hanlp.corpus.tag.Nature;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName="data4",type="words")    //分片修改
public class TeejoIntellicorriModelWords implements Serializable{

	@Id
	@Field(type=FieldType.Auto)
	private String id;
//    @Field(type=FieldType.Integer)
//    private String ns; // 地名
//	@Field(type=FieldType.Text)
//    private String nnt; // 职务职称
//	@Field(type=FieldType.Text)
//    private String nr; // 人名
//    @Field(type=FieldType.Text)
//    private String n; // 名词
//    @Field(type=FieldType.Text)
//    private String nz; // 其他专名
//    @Field(type=FieldType.Text)
//    private String nis; // 机构后缀
    @Field(type=FieldType.Text)
    private String wordsnature; // 词性
    @Field(type=FieldType.Text)
    private String wordsname; // 词性


    public TeejoIntellicorriModelWords() {

    }

    public TeejoIntellicorriModelWords(String id, String wordsnature, String wordsname) {
        this.id = id;
        this.wordsnature = wordsnature;
        this.wordsname = wordsname;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordsnature() {
        return wordsnature;
    }

    public void setWordsnature(String wordsnature) {
        this.wordsnature = wordsnature;
    }

    public String getWordsname() {
        return wordsname;
    }

    public void setWordsname(String wordsname) {
        this.wordsname = wordsname;
    }
}
