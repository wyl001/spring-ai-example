package cn.linux95.springaiexample.domain;

import lombok.Data;

import java.util.List;

@Data
public class Actor {
    private String name;
    private Integer age;
    private String sex;
    private List<Movie> movies;
}
@Data
class Movie{
    private String name;
    private String year;
    private String type;
}


