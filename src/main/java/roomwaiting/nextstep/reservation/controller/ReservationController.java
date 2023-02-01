package roomwaiting.nextstep.reservation.controller;

import roomwaiting.auth.principal.LoginMember;
import roomwaiting.auth.userdetail.UserDetails;
import java.net.URI;
import java.util.List;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(@LoginMember UserDetails member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(new Member(member), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).body("Location: /reservations/" + id);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationService.deleteById(new Member(member), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Reservation>> lookUpReservationWaitingList(@LoginMember UserDetails member) {
        List<Reservation> reservationList = reservationService.lookUp(new Member(member));
        return ResponseEntity.ok().body(reservationList);
    }
}