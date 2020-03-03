package com.cooperative.dao.user;

import com.cooperative.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUser(Long userId) {
        String sql = "select * from user where user_id=?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        Map map = jdbcTemplate.queryForMap(sql,userId);
        List<User> list = jdbcTemplate.query(sql,new UserRowMapper(),userId);
        return user;
    }

    static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setDepartmentId(rs.getInt("department_id"));
            return user;
        }
    }
}
