package com.william.williamRestApi.controllers;

import com.william.williamRestApi.TestDataUtil;
import com.william.williamRestApi.domain.dto.AuthorDto;
import com.william.williamRestApi.domain.entities.AuthorEntity;
import com.william.williamRestApi.services.AuthorService;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(
        scripts = "/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AuthorService authorService;


    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;

    }

    @Test
    public void testThatCreateAuthorReturnsHttp201Created() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsSavedAuthor() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("William Nwafor")

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(19)
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttp200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.saveAuthor(testAuthorA);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("William Nwafor")

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(19)
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttp200WhenAuthorExists() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.saveAuthor(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorsReturnsHttp404WhenNoAuthorExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAuthorsReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.saveAuthor(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testAuthorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("William Nwafor")

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(19)
        );
    }

    @Test
    public void testThatFullUpdateReturnsHttp404WhenNoAuthorExists() throws Exception {
        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
       String authorDtoJson =  objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateReturnsHttp200OkWhenAuthorExists() throws Exception {

       AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
       testAuthorEntityA.setId(null);
       AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntityA);

        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson =  objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)

        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        testAuthorEntityA.setId(null);
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntityA);

        AuthorEntity authorDto = TestDataUtil.createTestAuthorB();
        authorDto.setId(savedAuthor.getId());
        String authorDtoUpdateJson =  objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttp200OkWhenAuthorExists() throws Exception {

        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        testAuthorEntityA.setId(null);
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntityA);

        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
        testAuthorA.setName("UPDATED");
        String authorDtoJson =  objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)

        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        testAuthorEntityA.setId(null);
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntityA);

        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
        testAuthorA.setName("UPDATED");
        String authorDtoJson =  objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorA.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204NonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/123")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ExistingAuthor() throws Exception {

        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        testAuthorEntityA.setId(null);
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntityA);


        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}
