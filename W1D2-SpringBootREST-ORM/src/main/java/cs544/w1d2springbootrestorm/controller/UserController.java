package w1d2springbootrestorm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import w1d2springbootrestorm.dto.PostDTO;
import w1d2springbootrestorm.dto.UserDTO;
import w1d2springbootrestorm.dto.UserPostsDTO;
import w1d2springbootrestorm.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/{id}/posts")
    public UserPostsDTO getPosts(@PathVariable Long id) {
        return service.getUserPosts(id);
    }

    @PostMapping
    public UserDTO create(@RequestBody UserDTO dto) {
        return service.create(dto);
    }

    @PostMapping("/{id}/posts")
    public UserPostsDTO addPost(@PathVariable Long id, @RequestBody PostDTO postDto) {
        return service.addPostToUser(id, postDto);
    }
}
