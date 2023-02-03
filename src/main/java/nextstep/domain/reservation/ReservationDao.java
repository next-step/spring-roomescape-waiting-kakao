package nextstep.domain.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
            new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
            ),
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
            resultSet.getString("reservation.status"),
            resultSet.getInt("reservation.deposit")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, status, deposit, created_at) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            ps.setString(3, reservation.getStatus().name());
            ps.setInt(4, reservation.getDeposit());
            ps.setTimestamp(5, Timestamp.valueOf(reservation.getCreatedAt()));
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, reservation.deposit " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where theme.id = ? and schedule.date = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, reservation.deposit " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ?;";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean existsByScheduleId(Long id) {
        String sql = "SELECT 1 " +
                "FROM reservation " +
                "INNER JOIN schedule ON reservation.schedule_id = schedule.id " +
                "WHERE schedule.id = ?;";

        return jdbcTemplate.query(sql, ResultSet::next, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, reservation.deposit " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void updateReservationStatus(Long id, String status) {
        String sql = "UPDATE reservation SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }
}
