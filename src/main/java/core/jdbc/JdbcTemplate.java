package core.jdbc;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class JdbcTemplate<T> {

    public void update(PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
        ) {
            preparedStatementSetter.values(pstmt);

            pstmt.executeUpdate();
        }
    }

    public void update(Object... values) throws SQLException {
        update(ps -> {
            for (int i = 0; i < values.length; ++i) {
                ps.setObject(i + 1, values[i]);
            }
        });
    }

    public T queryForObject(RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        List<T> list = query(rowMapper, preparedStatementSetter);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<T> query(RowMapper<T> rowMapper) throws SQLException {
        return query(rowMapper, null);
    }

    public List<T> query(RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery())) {

            if(preparedStatementSetter != null) {
                preparedStatementSetter.values(pstmt);
            }

            return convertToList(rowMapper, pstmt);
        }
    }

    public List<T> convertToList(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                T findUser = rowMapper.mapRow(rs);
                list.add(findUser);
            }
        }
        return list;
    }

    abstract public String createQuery();
}
