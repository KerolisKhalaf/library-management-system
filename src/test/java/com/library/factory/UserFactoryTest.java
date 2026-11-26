package com.library.factory;

import com.library.model.Admin;
import com.library.model.RegularUser;
import com.library.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserFactoryTest {

    @Test
    void createsUsersFromTheirPersistedRoleNames() {
        User admin = UserFactory.createUser("Admin", "A1", "admin", "secret", "a@example.com");
        User regularUser = UserFactory.createUser(
            "Regular User", "U1", "reader", "secret", "u@example.com"
        );

        assertInstanceOf(Admin.class, admin);
        assertTrue(admin.isAdmin());
        assertInstanceOf(RegularUser.class, regularUser);
        assertFalse(regularUser.isAdmin());
    }

    @Test
    void rejectsUnknownUserRoles() {
        assertThrows(
            IllegalArgumentException.class,
            () -> UserFactory.createUser("Unknown", "1", "name", "secret", "u@example.com")
        );
    }
}
