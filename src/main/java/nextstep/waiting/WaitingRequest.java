package nextstep.waiting;

public class WaitingRequest {

    private Long scheduleId;

    public WaitingRequest() {
    }

    public WaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}