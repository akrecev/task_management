CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255)        NOT NULL,
    lastname  VARCHAR(255)        NOT NULL,
    email     VARCHAR(255) UNIQUE NOT NULL,
    password  VARCHAR(255)        NOT NULL,
    role      VARCHAR(50)         NOT NULL
);

CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(50) CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETE')),
    priority    VARCHAR(50) CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    author_id   BIGINT       NOT NULL,
    assignee_id BIGINT,
    CONSTRAINT fk_tasks_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE comments
(
    id        BIGSERIAL PRIMARY KEY,
    content   TEXT   NOT NULL,
    task_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_comments_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token_value VARCHAR(255) UNIQUE NOT NULL,
    token_type  VARCHAR(50) CHECK (token_type IN ('BEARER')) DEFAULT 'BEARER',
    revoked     BOOLEAN                                      DEFAULT FALSE,
    expired     BOOLEAN                                      DEFAULT FALSE,
    user_id     BIGINT,
    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
