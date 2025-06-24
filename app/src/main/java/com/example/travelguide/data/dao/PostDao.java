package com.example.travelguide.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.travelguide.data.entity.Post;
import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts ORDER BY createTime DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM posts WHERE id = :id")
    LiveData<Post> getPostById(int id);

    @Query("SELECT * FROM posts WHERE location LIKE :location ORDER BY createTime DESC")
    LiveData<List<Post>> getPostsByLocation(String location);

    @Query("SELECT * FROM posts WHERE title LIKE :title ORDER BY createTime DESC")
    LiveData<List<Post>> searchPostsByTitle(String title);

    @Insert
    long insertPost(Post post);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("DELETE FROM posts WHERE id = :id")
    void deletePostById(int id);

    @Query("SELECT COUNT(*) FROM posts")
    LiveData<Integer> getPostCount();
}