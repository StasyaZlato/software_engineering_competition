package com.example.task3.database;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostgresDao {
    <TEntry> Optional<TEntry> findById(long id);
    <TEntry> List<TEntry> findById(long id, BeanPropertyRowMapper<TEntry> mapper, String tableName);

    boolean delete(long id, String tableName);

    boolean delete(String tableName, Map<String, Object> conditions);

    <TEntry> List<TEntry> findAll();

    long add(Map<String, PromoDao.ParamWithType> stringParams, String tableName);
}

