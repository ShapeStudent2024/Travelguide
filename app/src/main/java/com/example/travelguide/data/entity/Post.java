package com.example.travelguide.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.travelguide.data.converter.StringListConverter;
import java.util.Date;
import java.util.List;

@Entity(tableName = "posts")
@TypeConverters({StringListConverter.class})
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private List<String> imagePaths;  // 图片路径列表
    private String location;
    private Date createTime;
    private Date updateTime;

    // 构造函数
    public Post() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public Post(String title, String content, List<String> imagePaths, String location) {
        this.title = title;
        this.content = content;
        this.imagePaths = imagePaths;
        this.location = location;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}