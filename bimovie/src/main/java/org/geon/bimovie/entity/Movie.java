package org.geon.bimovie.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "posterList")
@Table(name="tbl_movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;

    //연관관계의 주어 - FK 중심, PK 부가
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Poster> posterList;

    public void addPoster(Poster poster){
        if(posterList == null){
            posterList = new ArrayList<>();
        }
        poster.setIdx(posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }

    public void removePoster(Long ino){

        Optional<Poster> result = posterList.stream().filter(p -> p.getIno() == ino).findFirst();

        result.ifPresent( p -> {
            p.setMovie(null);
            posterList.remove(p);
        });
        changeIdx();
    }

    private void changeIdx() {
        for (int i = 0; i < posterList.size(); i++){
            posterList.get(i).setIdx(i);
        }
    }
}
