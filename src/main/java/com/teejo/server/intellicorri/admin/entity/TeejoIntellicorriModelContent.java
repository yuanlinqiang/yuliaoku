package com.teejo.server.intellicorri.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName="content_data",type="allLink")    //分片修改
public class TeejoIntellicorriModelContent implements Serializable{

	@Id
	@Field(type=FieldType.Auto)
	private String id;
    @Field(type=FieldType.Integer)
    private Integer sn; //序号
	@Field(type=FieldType.Text)
    private String url; // 所有子连接
	@Field(type=FieldType.Text)
    private String name; // 网站名称
	@Field(type=FieldType.Text)
    private String linename; // 栏目名称
    @Field(type=FieldType.Text)
    private String area; // 地区名称
    @Field(type=FieldType.Text)
    private String content; // 地区名称

    public TeejoIntellicorriModelContent() {
    }

    public TeejoIntellicorriModelContent(String id, String url, String name, String linename, Integer sn, String area,String  content) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.linename = linename;
        this.sn = sn;
        this.area = area;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
