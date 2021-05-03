package org.geon.bimovie.repository;

import lombok.extern.log4j.Log4j2;
import org.geon.bimovie.entity.Movie;
import org.geon.bimovie.entity.Poster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MovieRepositoryTests {

    @Autowired
    MovieRepository movieRepository;

    @Test
    public void testInsert(){

        Movie movie = Movie.builder().title("소울").build();

        Poster p1 = Poster.builder().fname("소울 포스터1.jpg").build();
        Poster p2 = Poster.builder().fname("소울 포스터2.jpg").build();

        movie.addPoster(p1);
        movie.addPoster(p2);

        movieRepository.save(movie);
    }

    @Test
    public void testAddPoster(){

        Movie movie = movieRepository.findById(1L).get();

        movie.addPoster(Poster.builder().fname("소울 포스터3.jpg").build());

        movieRepository.save(movie);
    }

    @Test
    @Transactional
    @Commit
    public void testRemovePoster() {

        Movie movie = movieRepository.getOne(1L);

        movie.removePoster(2L);

        movieRepository.save(movie);
    }


    @Test
    public void insertMovies() {

        IntStream.rangeClosed(10,100).forEach(i -> {
            Movie movie = Movie.builder().title("해리포터"+i).build();
            movie.addPoster(Poster.builder().fname("해리포터"+i+" 포스터1.jpg").build());
            movie.addPoster(Poster.builder().fname("해리포터"+i+" 포스터2.jpg").build());

            movieRepository.save(movie);
        });
    }

    @Test
    @Transactional
    @Commit
    //쿼리가 여러번 날아감 못쓰는코드
    public void testPaging1() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Movie> result = movieRepository.findAll(pageable);


        result.getContent().forEach(m -> {
            log.info(m.getMno());
            log.info(m.getTitle());
            log.info(m.getPosterList().size());
            log.info("----------------------------------");
        });
    }

    @Test
    //리밋이 걸리지않음
    public void testPaging2All() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Movie> result = movieRepository.findAll2(pageable);


        result.getContent().forEach(m -> {
            log.info(m.getMno());
            log.info(m.getTitle());
            log.info(m.getPosterList());
            log.info("----------------------------------");
        });
    }

    @Test
    public void testPaging3All() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.findAll3(pageable);


        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
            log.info("----------------------------------");
        });
    }
}
