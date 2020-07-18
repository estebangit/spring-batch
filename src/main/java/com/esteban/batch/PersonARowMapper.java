package com.esteban.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonARowMapper implements RowMapper<PersonA> {

    @Override
    public PersonA mapRow(ResultSet rs, int rowNum) throws SQLException {
        final PersonA bean = new PersonA();
        bean.setFirstName(rs.getString("first_name"));
        bean.setLastName(rs.getString("last_name"));
        return bean;
    }

}
