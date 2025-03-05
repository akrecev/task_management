package ru.kretsev.model.comment;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import ru.kretsev.model.task.Task;
import ru.kretsev.model.user.User;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", content='" + content + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
