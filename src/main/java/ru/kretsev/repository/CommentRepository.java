package ru.kretsev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kretsev.model.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {}
