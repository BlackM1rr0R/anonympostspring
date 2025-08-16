package org.example.blogback.service;


import org.example.blogback.dto.PostResponse;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Saved;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.SavedRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedService {
    private final SavedRepository savedRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public SavedService(SavedRepository savedRepository, UserRepository userRepository, PostRepository postRepository) {
        this.savedRepository = savedRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public boolean savePost(Saved saved) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Users user=userRepository.findByUsername(username);
        if(saved.getPost()==null||saved.getPost().getId()==null){
            throw new RuntimeException("Post is null");
        }
        Post post=postRepository.findById(saved.getPost().getId()).orElse(null);
        Optional<Saved> existingSaved=savedRepository.findByUserIdAndPostId(user.getId(), saved.getPost().getId());
        if(existingSaved.isPresent()){
            savedRepository.delete(existingSaved.get());
            return false;
        }else{
            Saved newSave=new Saved();
            newSave.setUser(user);
            newSave.setPost(post);
            savedRepository.save(newSave);
            return true;
        }
    }




    public List<PostResponse> getCurrentUserSaved() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");

        return savedRepository.findAllByUser_Id(user.getId()).stream()
                .map(Saved::getPost)
                .filter(p -> p != null)
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getAuthor(), p.getImageUrl()))
                .collect(Collectors.toList());
    }
}
