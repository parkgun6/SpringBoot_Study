package org.geon.club.repository;

import org.geon.club.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select n from Note n where n.num = :num")
    Optional<Note> getWithWriter(@Param("num") Long num);

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select n from Note n where n.writer.email = :email")
    List<Note> getList(@Param("email") String email);
}
