package w1d2springbootrestorm.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPostsDTO {
    private Long id;
    private String name;
    private List<PostDTO> posts;
}
