package com.example.task3.database;

import com.example.task3.database.entities.Participant;
import com.example.task3.database.entities.Prize;
import com.example.task3.database.entities.Promo;
import com.example.task3.database.entities.PromoBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class PromoDao extends JdbcDaoSupport implements PostgresDao {
    public static final String TABLE_NAME = "promo";
    public static final String PRIZES_TABLE_NAME = "prizes";
    public static final String PARTICIPANTS_TABLE_NAME = "participants";

    @Autowired
    public PromoDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public Optional<Promo> findById(long id) {
        Optional<PromoBase> promo = findById(id, new BeanPropertyRowMapper<>(PromoBase.class), TABLE_NAME)
                .stream().findFirst();

        return promo.map(x -> {
            List<Prize> prizes = findWhereLongField("promo_id", id, new BeanPropertyRowMapper<>(Prize.class),
                    PRIZES_TABLE_NAME);
            List<Participant> participants = findWhereLongField("promo_id", id,
                    new BeanPropertyRowMapper<>(Participant.class), PARTICIPANTS_TABLE_NAME);

            return new Promo(x, prizes, participants);
        });
    }

    public boolean updatePromo(long id, Map<String, String> stringParams) {
        String setExpr = stringParams.entrySet().stream().map(x -> x.getKey() + " = \'" + x.getValue() + "\'").collect(Collectors.joining(", "));
        return getJdbcTemplate().update("UPDATE " + TABLE_NAME + " SET (" + setExpr + ") WHERE id = " + id) > 0;
    }

    @Override
    public <TEntry> List<TEntry> findById(long id, BeanPropertyRowMapper<TEntry> mapper, String tableName) {
        return getJdbcTemplate().query("SELECT * FROM " + tableName + " WHERE id = ?",
                new Object[]{id},
                new int[]{Types.BIGINT},
                mapper);
    }

    @Override
    public boolean delete(long id, String tableName) {
        return getJdbcTemplate().update("DELETE FROM " + tableName + " WHERE id = " + id) > 0;
    }

    @Override
    public boolean delete(String tableName, Map<String, Object> conditions) {
        String conditionsString = conditions.entrySet().stream().map(x -> x.getKey() + " = " + x.getValue()).collect(Collectors.joining(" AND "));
        return getJdbcTemplate().update("DELETE FROM " + tableName + " WHERE " + conditionsString) > 0;
    }

    private <TEntry> List<TEntry> findWhereLongField(String fieldName, long value,
                                                     BeanPropertyRowMapper<TEntry> mapper, String tableName) {
        return getJdbcTemplate().query("SELECT * FROM " + tableName + " WHERE " + fieldName + " = ?",
                new Object[]{value},
                new int[]{Types.BIGINT},
                mapper);
    }

    @Override
    public List<PromoBase> findAll() {
        return getJdbcTemplate().query("SELECT * FROM " + TABLE_NAME, new BeanPropertyRowMapper<>(PromoBase.class));
    }

    @Override
    public long add(Map<String, ParamWithType> params, String tableName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        List<String> paramsNames = new ArrayList<>(params.keySet());
        List<String> paramsHolders = paramsNames.stream().map(x -> "?").collect(Collectors.toList());
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", paramsNames) + ") values (" +
                String.join(", ", paramsHolders) + ")";

        PreparedStatementCreatorFactory psFactory = new PreparedStatementCreatorFactory(insertQuery,
                paramsNames.stream().map(x -> params.get(x).type).mapToInt(i -> i).toArray());
        psFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = psFactory.newPreparedStatementCreator(paramsNames.stream().map(x -> params.get(x).value)
                .collect(Collectors.toList()));

        getJdbcTemplate().update(psc, keyHolder);

        Long newId;
        if (keyHolder.getKeys().size() > 1) {
            newId = (Long) keyHolder.getKeys().get("id");
        } else {
            newId = keyHolder.getKey().longValue();
        }
        return newId;
    }

    public static class ParamWithType {
        public final Object value;
        public final int type;

        public ParamWithType(Object value, int type) {
            this.value = value;
            this.type = type;
        }
    }
}
