package model;

import lombok.Data;

@Data
public class BlogEntry {

    private Integer id;
    private String text;
    private Integer userId;
}
