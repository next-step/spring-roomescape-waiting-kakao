package nextstep.reservationwaiting.domain;

import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;

import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(member.getId(), member.getId());
    }
}