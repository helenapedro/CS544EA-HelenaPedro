package w1d2springbootrestorm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import w1d2springbootrestorm.dto.PostDTO;
import w1d2springbootrestorm.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public List<PostDTO> getAll() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDTO getById(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @PostMapping
    public PostDTO create(@RequestBody PostDTO dto) {
        return service.create(dto);
    }
}
