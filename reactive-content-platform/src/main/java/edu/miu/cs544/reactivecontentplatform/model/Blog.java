package edu.miu.cs544.reactivecontentplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blogs")
public class Blog {
    @Id
    private String id;

    private String title;
    private String author;

    private String categoryId;

    @Transient
    private Category category;
}
