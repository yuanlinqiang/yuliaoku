package com.teejo.server.intellicorri.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Document(indexName="address_data",type="address")    //分片修改
public class TeejoIntellicorriModelAddress implements Serializable{

	@Id
	@Field(type=FieldType.Auto)
	private String id;
    @Field(type=FieldType.Integer )
    private Integer sn; //序号
	@Field(type=FieldType.Text)
    private String name; // 人物名称
	@Field(type=FieldType.Text)
    private String area; // 所属地区
	@Field(type=FieldType.Text)
    private String datasource; // 数据来源
    @Field(type=FieldType.Text)
    private String linename; // 所属栏目
    @Field(type=FieldType.Text)
    private String islabel; // 是否标注
    @Field(type=FieldType.Text)
    private String type; // 分类
    @Field(type=FieldType.Date)
    private Timestamp createtime; // 创建时间
    @Field(type=FieldType.Date)
    private Timestamp labeltime; // 标注时间
    @Field(type=FieldType.Text)
    private String labeltor; //标注人
    @Field(type=FieldType.Text)
    private String remark; // 备注


    public TeejoIntellicorriModelAddress() {
    }

    public TeejoIntellicorriModelAddress(String id, String area, String name, String linename, Integer sn, String datasource, String islabel, String type, Timestamp createtime, Timestamp labeltime, String labeltor, String remark) {
        this.id = id;
        this.area = area;
        this.name = name;
        this.linename = linename;
        this.sn = sn;
        this.datasource = datasource;
        this.islabel = islabel;
        this.type = type;
        this.createtime = createtime;
        this.labeltime = labeltime;
        this.labeltor = labeltor;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getIslabel() {
        return islabel;
    }

    public void setIslabel(String islabel) {
        this.islabel = islabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getLabeltime() {
        return labeltime;
    }

    public void setLabeltime(Timestamp labeltime) {
        this.labeltime = labeltime;
    }

    public String getLabeltor() {
        return labeltor;
    }

    public void setLabeltor(String labeltor) {
        this.labeltor = labeltor;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
