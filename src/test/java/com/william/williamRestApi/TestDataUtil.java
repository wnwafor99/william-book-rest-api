package com.william.williamRestApi;

import com.william.williamRestApi.domain.dto.AuthorDto;
import com.william.williamRestApi.domain.dto.BookDto;
import com.william.williamRestApi.domain.entities.AuthorEntity;
import com.william.williamRestApi.domain.entities.BookEntity;

public class TestDataUtil {
private TestDataUtil(){
}

public static AuthorEntity createTestAuthorA() {
    return AuthorEntity.builder()
            .id(1L)
            .name("William Nwafor")
            .age(19)
            .build();
}

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("William Nwafor")
                .age(19)
                .build();
    }

public static AuthorEntity createTestAuthorB() {
    return AuthorEntity.builder()
            .id(2L)
            .name("Abigail Rose")
            .age(80)
            .build();
}

public static AuthorEntity createTestAuthorC() {
    return AuthorEntity.builder()
            .id(3L)
            .name("Lotanna Iwuala")
            .age(18)
            .build();
}

public static BookEntity createTestBookA(final AuthorEntity author) {
    return BookEntity.builder()
            .isbn("978-1-2345-6789-0")
            .title("The Shadow in the Attic")
            .authorEntity(author)
            .build();
}

    public static BookDto createTestBookDtoA(final AuthorDto author) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .authorDto(author)
                .build();
    }

public static BookEntity createTestBookB(final AuthorEntity author) {
    return BookEntity.builder()
            .isbn("12345-6789-0")
            .title("The Book of Books")
            .authorEntity(author)
            .build();
}
public static BookEntity createTestBookC(final AuthorEntity author) {
    return BookEntity.builder()
            .isbn("09876-5432-1")
            .title("The Dih Book")
            .authorEntity(author)
            .build();
}

}
