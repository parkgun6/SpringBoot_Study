package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    @Commit
    @Transactional
    @Test
    public void insertMovies(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            Movie movie = Movie.builder().title("Movie..." + i).build();

            log.info("--------------------------------------------");
            //번호 추출
            movieRepository.save(movie);
            //최대 5개까지 이미지 등록
            int count = (int)(Math.random() * 5) + 1;

            for (int j = 0; j < count; j++){

                boolean profile = j == 0;

                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg")
                        .path("2021\\4\\26")
                        .profile(profile)
                        .build();

                imageRepository.save(movieImage);
            }//end for
        });
    }

    @Test
    public void testList1(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
    }

    @Test
    public void testWithAll() {

        Long mno = 200L;

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        result.forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
    }
}
