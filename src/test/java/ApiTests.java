import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class ApiTests {

    @Test
    public void listUsersTest() {
        given().get("https://reqres.in/api/users?page=2").
                then().statusCode(200).
                and().body("per_page", is(6));
    }

    @Test
    public void createUser() {
        given().contentType(ContentType.JSON)
                .body("{\"name\": \"dima\"," +
                        "\"job\": \"QA\"}")
                .when().post("https://reqres.in/api/users")
                .then().statusCode(201)
                .body("name", is("dima"))
                .body("job", is("QA"));
    }

    @Test
    public void register() {
        String token = given().contentType(ContentType.JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"pistol\"}")
                .when().post("https://reqres.in/api/register")
                .then().statusCode(200)
                .extract().path("token");
        StringUtils.isNoneEmpty(token);
    }

    @Test
    void registerUnsuccessful() {
        given().contentType(ContentType.JSON)
                .body("{\"email\": \"dima@mail.ru\"}")
                .when().post("https://reqres.in/api/register")
                .then().statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void checkWdHubStatus() {
        given().auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then().statusCode(200)
                .and().body("value.ready", is(true));
    }
}
