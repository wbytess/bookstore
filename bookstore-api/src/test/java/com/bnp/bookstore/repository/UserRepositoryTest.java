package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bnp.bookstore.model.User;
import com.bnp.bookstore.model.UserRole;

@DataJpaTest
class UserRepositoryTest {

	private static final String PASSWORD = "password";
	private static final String TEST_EMAIL = "test@example.com";
	private static final String TESTUSER = "testuser";

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("should persist user with auto generated id and timestamp")
    void shouldPersistUser() {
        User savedUser = createUser(TESTUSER, TEST_EMAIL);

        assertAll(
                () -> assertNotNull(savedUser.getId()),
                () -> assertEquals(TESTUSER, savedUser.getUsername()),
                () -> assertEquals(TEST_EMAIL, savedUser.getEmail()),
                () -> assertNotNull(savedUser.getCreatedAt())
        );
    }

    
    private User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PASSWORD);
        user.setRole(UserRole.USER);
        User saved = userRepository.save(user);
        entityManager.flush();
        return saved;
    }
}
