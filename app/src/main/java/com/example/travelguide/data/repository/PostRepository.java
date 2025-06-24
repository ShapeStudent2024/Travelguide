package com.example.travelguide.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.travelguide.data.dao.PostDao;
import com.example.travelguide.data.database.TravelDatabase;
import com.example.travelguide.data.entity.Post;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostRepository {

    private PostDao postDao;
    private LiveData<List<Post>> allPosts;
    private ExecutorService executor;

    public PostRepository(Application application) {
        TravelDatabase database = TravelDatabase.getDatabase(application);
        postDao = database.postDao();
        allPosts = postDao.getAllPosts();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    public LiveData<Post> getPostById(int id) {
        return postDao.getPostById(id);
    }

    public LiveData<List<Post>> getPostsByLocation(String location) {
        return postDao.getPostsByLocation("%" + location + "%");
    }

    public LiveData<List<Post>> searchPostsByTitle(String title) {
        return postDao.searchPostsByTitle("%" + title + "%");
    }

    public void insertPost(Post post) {
        executor.execute(() -> {
            postDao.insertPost(post);
        });
    }

    public void updatePost(Post post) {
        executor.execute(() -> {
            postDao.updatePost(post);
        });
    }

    public void deletePost(Post post) {
        executor.execute(() -> {
            postDao.deletePost(post);
        });
    }

    public void deletePostById(int id) {
        executor.execute(() -> {
            postDao.deletePostById(id);
        });
    }

    public LiveData<Integer> getPostCount() {
        return postDao.getPostCount();
    }
}