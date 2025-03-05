package ru.kretsev.model.task;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import ru.kretsev.model.comment.Comment;
import ru.kretsev.model.user.User;

/**
 * Entity representing a task in the system.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Task{" + "id="
                + id + ", title='"
                + title + '\'' + ", description='"
                + description + '\'' + ", status="
                + status + ", priority="
                + priority + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
