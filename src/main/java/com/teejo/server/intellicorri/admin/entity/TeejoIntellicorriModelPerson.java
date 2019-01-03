package com.teejo.server.intellicorri.admin.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

/**
 * @Title: Person
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/8/16 9:44
 */
@Data
@Document(indexName="data",type="person")
public class TeejoIntellicorriModelPerson implements Serializable{
    
	@Id
	@Field(type=FieldType.Auto)
	private String id;
	@Field(type=FieldType.Text)
    private String name;
	@Field(type=FieldType.Integer)
    private Integer age;
	@Field(type=FieldType.Text)
    private String work;

    public TeejoIntellicorriModelPerson() {
    }

    public TeejoIntellicorriModelPerson(String id, String name, Integer age, String work) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.work = work;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
