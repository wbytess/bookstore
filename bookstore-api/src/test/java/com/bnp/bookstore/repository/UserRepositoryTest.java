package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
	private static final String DEV_USER = "devuser";

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
    
    @Test
    @DisplayName("given existing email when finding user then user is returned")
    void givenEmailThenFindingUserThenUserIsReturned() {
        createUser(TESTUSER, TEST_EMAIL);
        Optional<User> found = userRepository.findByEmail(TEST_EMAIL);

        assertTrue(found.isPresent());
        assertEquals(TEST_EMAIL, found.get().getEmail());
    }

    @Test
    @DisplayName("given username when checking existence then true or false is returned")
    void givenUsernameWhenCheckingExistenceThenCorrectResultIsReturned() {
    	createUser(TESTUSER, TEST_EMAIL);

        boolean exists = userRepository.existsByUsername(TESTUSER);
        boolean notExists = userRepository.existsByUsername(DEV_USER);

        assertTrue(exists);
        assertFalse(notExists);
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
