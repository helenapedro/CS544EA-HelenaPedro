package w1d2springbootrestorm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import w1d2springbootrestorm.dto.PostDTO;
import w1d2springbootrestorm.entitie.Post;
import w1d2springbootrestorm.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PostDTO.class))
                .toList();
    }

    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return modelMapper.map(post, PostDTO.class);
    }

    public PostDTO create(PostDTO dto) {
        Post post = modelMapper.map(dto, Post.class);
        return modelMapper.map(postRepository.save(post), PostDTO.class);
    }
}
