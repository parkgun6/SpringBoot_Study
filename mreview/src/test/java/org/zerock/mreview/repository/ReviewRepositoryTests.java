package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertReviews() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            Long mno = (long) (Math.random() * 100) + 101;

            Long mid = (long) (Math.random() * 100) + 1;

            Movie movie = Movie.builder().mno(mno).build();
            Member member = Member.builder().mid(mid).build();

            int grade = (int) (Math.random() * 5) + 1;

            Review review = Review.builder()
                    .member(member)
                    .movie(movie)
                    .grade(grade)
                    .text("이 영화에 대한 느낌은..." + i)
                    .build();

            reviewRepository.save(review);
        });
    }

//  @Transactional
    @Test
    public void testFindByMovie() {
        Long mno = 200L;
        Movie movie = Movie.builder().mno(mno).build();

        reviewRepository.findByMovie(movie).forEach(review -> {
            log.info(review);
            //현재는 LAZY로딩이기 때문에 Member쿼리가 여러번 날아간다.
            log.info(review.getMember());

        });
    }

    @Test
    public void testFindWithMovie() {
        Long mno = 200L;

        reviewRepository.findWithMovie(mno).forEach(review -> log.info(review));
    }
}
