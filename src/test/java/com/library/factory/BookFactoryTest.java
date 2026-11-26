package com.library.factory;

import com.library.model.AIBook;
import com.library.model.Book;
import com.library.model.ManagementBook;
import com.library.model.SoftwareEngineeringBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookFactoryTest {

    @Test
    void createsBookTypesFromTheirPersistedCategoryNames() {
        Book softwareEngineering = BookFactory.createBook(
            "Software Engineering", "SE-1", "Software Design", "Author", 2024
        );
        Book management = BookFactory.createBook(
            "Management", "MG-1", "Managing Teams", "Author", 2023
        );
        Book artificialIntelligence = BookFactory.createBook(
            "Artificial Intelligence", "AI-1", "Machine Learning", "Author", 2025
        );

        assertInstanceOf(SoftwareEngineeringBook.class, softwareEngineering);
        assertInstanceOf(ManagementBook.class, management);
        assertInstanceOf(AIBook.class, artificialIntelligence);
        assertEquals("Software Engineering", softwareEngineering.getCategory());
        assertEquals("Management", management.getCategory());
        assertEquals("Artificial Intelligence", artificialIntelligence.getCategory());
    }

    @Test
    void rejectsUnknownBookCategories() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BookFactory.createBook("Unknown", "1", "Title", "Author", 2024)
        );
    }
}
