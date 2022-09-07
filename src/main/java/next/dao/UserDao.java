package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        insertJdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        updateJdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.query("SELECT userId, password, name, email FROM USERS", this::mapRowForFindAll);
    }

    private <T> List<User> mapRowForFindAll(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
            users.add(user);
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                pstmt -> setValuesForFindById(userId, pstmt), this::mapRowForFindById);
    }

    void setValuesForFindById(String userId, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, userId);
    }

    private <T> User mapRowForFindById(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
        }
        return null;
    }
}
