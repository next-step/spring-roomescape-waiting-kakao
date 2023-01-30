package nextstep.reservationwaitings;

import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingsController {

    private final ReservationWaitingsService reservationWaitingsService;

    public ReservationWaitingsController(ReservationWaitingsService reservationWaitingsService) {
        this.reservationWaitingsService = reservationWaitingsService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationWaitingRequest request) {
        long id = reservationWaitingsService.create(member, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }
}
