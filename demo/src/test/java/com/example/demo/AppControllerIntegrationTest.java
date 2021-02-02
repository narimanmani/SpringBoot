package com.example.demo;

import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.PostConstruct;
import static org.mockito.Mockito.*;
import com.example.demo.api.PersonController;
import com.example.demo.model.Person;
import org.springframework.http.HttpStatus;
import java.util.UUID;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @MockBean
    PersonController appService;

    @Test
    public void apiTest_getById() {
        UUID id = UUID.randomUUID();
        Person testPerson = new Person(id, "Nariman Mani");
        when(appService.getPersonById(id)).thenReturn(testPerson);

        get(uri + "/api/v1/person/" + testPerson.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(testPerson.getId().toString()))
                .body("name", equalTo(testPerson.getName()));

    }

    @Test
    public void apiTest_Post() {

        JSONObject request = new JSONObject();
        request.put("name", "Nariman Mani");


        given().contentType("application/json").
                body(request.toJSONString()).
                when().
        post(uri + "/api/v1/person").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void apiTest_Delete() {

        UUID id = UUID.randomUUID();
        JSONObject request = new JSONObject();
        given().contentType("application/json").
                body(request.toJSONString()).
                when().
                delete(uri + "/api/v1/person/"+id.toString()).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

    }
}
