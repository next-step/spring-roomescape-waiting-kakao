package roomwaiting.nextstep.schedule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/admin/schedules")
    public ResponseEntity<String> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).body("Location: /schedules/" + id);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }

    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}