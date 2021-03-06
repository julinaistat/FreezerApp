package com.example.freezerapp3;

import com.example.freezerapp3.entity.Food;
import com.example.freezerapp3.entity.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcFood {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // We can create a custom RowMapper
    class FoodRowMapper implements RowMapper<Food>{
        @Override
        public  Food mapRow(ResultSet rs, int rowNum) throws SQLException {
            Food food = new Food();
            food.setName(rs.getString("name"));
            food.setWeight(rs.getDouble("weight"));
            food.setUnitOfMeasure(rs.getString("unit_of_measure"));
            //food.setUnitOfMeasure(Unit.valueOf(rs.getString("unit_of_measure")));
            food.setDateOfStorage(rs.getTimestamp("date_of_storage"));
            return food;
        }

    }


    public List<Food> findAll(){
        return jdbcTemplate.query("select * from food",
                //new BeanPropertyRowMapper<Food>(Food.class));
                new FoodRowMapper());
    };

    public Food findByName(String name){
        return jdbcTemplate.queryForObject("select * from food where name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<Food>(Food.class));
    };

    public int deleteByName(String name){
        return jdbcTemplate.update("delete from food where name=?",
                new Object[]{name});
    };

    public int insert(Food food) {
        return jdbcTemplate.update(
                "insert into food (name, weight, unit_of_measure, date_of_storage)"
                + "values(?, ?, ?, ?)",
                new Object[]{
                        food.getName(), food.getWeight(), food.getUnitOfMeasure(), food.getDateOfStorage()
                }
        );
    }

    public int update(Food food) {
        return jdbcTemplate.update(
                "update food "
                + "set weight = ?, unit_of_measure = ?, date_of_storage = ? "
                + "where name = ?",
                new Object[]{
                        food.getWeight(), food.getUnitOfMeasure(), food.getDateOfStorage()
                }
        );
    }

}
