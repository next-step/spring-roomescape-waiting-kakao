package nextstep.reservation;

import auth.exception.AuthenticationException;
import auth.config.LoginMember;
import auth.config.MemberDetails;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        checkValid(member);
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        checkValid(member);
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    private static void checkValid(MemberDetails memberDetails) {
        if (Objects.isNull(memberDetails)) {
            throw new AuthenticationException();
        }
    }
}
