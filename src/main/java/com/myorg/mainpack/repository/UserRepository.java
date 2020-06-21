package com.myorg.mainpack.repository;

import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Repository
public class UserRepository {

    private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private JdbcTemplate jdbcTemplate;
    public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final SimpleJdbcInsert insertUser;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }



    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM USERS WHERE username = ?", ROW_MAPPER, username);
        return setRoles(DataAccessUtils.singleResult(users));
    }


    public List<User> findAll() {
        Map<Long, Set<Role>> map = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            map.computeIfAbsent(rs.getLong("user_id"), userId -> EnumSet.noneOf(Role.class))
                    .add(Role.valueOf(rs.getString("role")));
        });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY id DESC", ROW_MAPPER);
        users.forEach( u -> u.setRoles( map.get( u.getId() ) ) );
        return users;
    }


    @Transactional
    public User save(User user){

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        Number newKey = insertUser.executeAndReturnKey(parameterSource);
        user.setId(newKey.longValue());
        insertRoles(user);

        return user;

    }


    @Transactional
    public boolean delete(Long id){
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }


    private void insertRoles(User u) {
        Set<Role> roles = u.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                    (ps, role) -> {
                        ps.setLong(1, u.getId());
                        ps.setString(2, role.name());
                    });
        }
    }


    private void deleteRoles(User u) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", u.getId());
    }


    private User setRoles(User u) {
        if (u != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles  WHERE user_id=?", Role.class, u.getId());
            u.setRoles( new HashSet<>(roles));
        }
        return u;
    }


}
