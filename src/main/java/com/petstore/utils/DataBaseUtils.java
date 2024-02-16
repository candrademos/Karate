package com.petstore.utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.Map;

public class DataBaseUtils {

    private static final Logger logger = Logger.getLogger(DataBaseUtils.class.getName());

    private final JdbcTemplate jdbc;

    public DataBaseUtils(Map<String, Object> config) {
        if("not_supported".equals(config.get("driverClassName"))){
                throw new UnsupportedOperationException();
        } else {
            String url = (String) config.get("url");
            String username = (String) config.get("username");
            String password = (String) config.get("password");
            String driver = (String) config.get("driverClassName");
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            jdbc = new JdbcTemplate(dataSource);
            logger.log(Level.INFO, "init jdbc template: {}", url);
        }
    }

    public Object readValue(String query) {
        return jdbc.queryForObject(query, Object.class);
    }

    public Map<String, Object> readRow(String query) {
        return jdbc.queryForMap(query);
    }

    public List<Map<String, Object>> readRows(String query) {
        return jdbc.queryForList(query);
    }

    public int update(String query, Object... args ){
        return jdbc.update(query, args);
    }

}