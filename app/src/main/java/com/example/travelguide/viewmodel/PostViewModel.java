package com.example.travelguide.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.travelguide.data.entity.Post;
import com.example.travelguide.data.repository.PostRepository;
import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;
    private LiveData<List<Post>> allPosts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        allPosts = repository.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    public LiveData<Post> getPostById(int id) {
        return repository.getPostById(id);
    }

    public LiveData<List<Post>> getPostsByLocation(String location) {
        return repository.getPostsByLocation(location);
    }

    public LiveData<List<Post>> searchPostsByTitle(String title) {
        return repository.searchPostsByTitle(title);
    }

    public void insertPost(Post post) {
        repository.insertPost(post);
    }

    public void updatePost(Post post) {
        repository.updatePost(post);
    }

    public void deletePost(Post post) {
        repository.deletePost(post);
    }

    public void deletePostById(int id) {
        repository.deletePostById(id);
    }

    public LiveData<Integer> getPostCount() {
        return repository.getPostCount();
    }
}