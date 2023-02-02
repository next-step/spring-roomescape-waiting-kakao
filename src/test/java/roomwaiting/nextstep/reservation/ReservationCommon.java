package roomwaiting.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import roomwaiting.nextstep.AbstractE2ETest;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.ScheduleRequest;
import roomwaiting.nextstep.theme.ThemeRequest;


public class ReservationCommon extends AbstractE2ETest {
    public static final String TEST_DATE = "2022-08-11";
    public static final String TEST_TIME = "13:00:00";

    protected ReservationRequest request;
    protected Long themeId;
    protected Long scheduleId;


    @BeforeEach
    protected void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000L);
        var themeResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, TEST_DATE, TEST_TIME);
        var scheduleResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        request = new ReservationRequest(
                scheduleId
        );
    }

    protected ExtractableResponse<Response> lookUpReservation(String accessToken) {
        return RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .auth().oauth2(accessToken)
                .when().get("/reservations")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> requestCreateReservation(ReservationRequest requestBody, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> requestApprove(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/approve")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> cancelReservation(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/cancel")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> requestCancelApprove(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/cancel-approve")
                .then().log().all()
                .extract();
    }
}