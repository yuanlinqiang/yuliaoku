package com.teejo.server.intellicorri.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName="data2",type="crawling")    //分片修改
public class TeejoIntellicorriModelCrawling implements Serializable{

	@Id
	@Field(type=FieldType.Auto)
	private String id;
	@Field(type=FieldType.Text)
    private String url; // 新闻网址
	@Field(type=FieldType.Text)
    private String name; // 网址名称
	@Field(type=FieldType.Text)
    private String linename; // 栏目名称
	@Field(type=FieldType.Text)
    private String area; // 所属地区
    @Field(type=FieldType.Integer)
    private Integer sn; //序号

    public TeejoIntellicorriModelCrawling() {
    }

    public TeejoIntellicorriModelCrawling(String id, String url, String name, String linename, Integer sn ,String area) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.linename = linename;
        this.sn = sn;
        this.area = area;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
