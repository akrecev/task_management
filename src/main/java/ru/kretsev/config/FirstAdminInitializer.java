package ru.kretsev.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kretsev.model.user.Role;
import ru.kretsev.model.user.User;
import ru.kretsev.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirstAdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${first-admin.firstname}")
    private String firstname;

    @Value("${first-admin.lastname}")
    private String lastname;

    @Value("${first-admin.email}")
    private String email;

    @Value("${first-admin.password}")
    private String password;

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(Role.ROLE_ADMIN).isEmpty()) {
            User admin = User.builder()
                    .firstname(firstname)
                    .lastname(lastname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Создан первый администратор: admin@example.com");
        }
    }
}
