package com.william.williamRestApi.controllers;

import com.william.williamRestApi.TestDataUtil;
import com.william.williamRestApi.domain.dto.BookDto;
import com.william.williamRestApi.domain.entities.BookEntity;
import com.william.williamRestApi.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(
        scripts = "/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper,BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;

    }

    @Test
    public void testThatCreateUpdateBookRequestReturnsHttpStatus201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
       String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }

    @Test
    public void testThatCreateBookRequestReturnsCreatedUpdateBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );

    }

    @Test
    public void testThatListBooksReturnsHttpStatus200k() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsBooks() throws Exception {

        BookEntity testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(testBookEntity.getTitle()));
    }

    @Test
    public void testThatGetBooksReturnsHttpStatus200Ok() throws Exception {

        BookEntity testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBooksReturnsHttpStatus404WhenBookDoesNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatBooksReturnsBookWhenItExists() throws Exception {

        BookEntity testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatCreateUpdateBookRequestReturnsHttpStatus200Ok() throws Exception {
        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntityA = bookService.createUpdateBook(bookEntityA.getIsbn(), bookEntityA);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setIsbn(savedBookEntityA.getIsbn());
        String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );


    }

    @Test
    public void testThatUpdateBooksReturnsBooks() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntityA = bookService.createUpdateBook(bookEntityA.getIsbn(), bookEntityA);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setIsbn(savedBookEntityA.getIsbn());
        bookDto.setTitle("UPDATED");
        String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+savedBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)


        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntityA.getIsbn())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }

    @Test
    public void testThatPartialUpdateBookRequestReturnsHttpStatus200Ok() throws Exception {
        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntityA = bookService.createUpdateBook(bookEntityA.getIsbn(), bookEntityA);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("UPDATED");
        String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );


    }

    @Test
    public void testThatPartialUpdateBooksReturnsBooks() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntityA = bookService.createUpdateBook(bookEntityA.getIsbn(), bookEntityA);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("UPDATED");
        String createBookJson =  objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+savedBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)


        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntityA.getIsbn())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }

    @Test
    public void testThatDeleteBookRequestReturnsHttpStatus204NonExistingBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)


        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBooksReturnsHttpStatus204ExistingBook() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntityA = bookService.createUpdateBook(bookEntityA.getIsbn(), bookEntityA);


        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/"+savedBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)


        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
