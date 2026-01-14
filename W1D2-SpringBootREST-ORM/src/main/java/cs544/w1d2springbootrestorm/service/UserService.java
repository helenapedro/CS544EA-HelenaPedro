package w1d2springbootrestorm.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import w1d2springbootrestorm.dto.PostDTO;
import w1d2springbootrestorm.dto.UserDTO;
import w1d2springbootrestorm.dto.UserPostsDTO;
import w1d2springbootrestorm.entitie.Post;
import w1d2springbootrestorm.entitie.User;
import w1d2springbootrestorm.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO create(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    public UserPostsDTO getUserPosts(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserPostsDTO.class);
    }

    public UserPostsDTO addPostToUser(Long userId, PostDTO postDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = modelMapper.map(postDto, Post.class);
        user.getPosts().add(post); // because of @JoinColumn, Post gets user_id
        User saved = userRepository.save(user);

        return modelMapper.map(saved, UserPostsDTO.class);
    }
}
