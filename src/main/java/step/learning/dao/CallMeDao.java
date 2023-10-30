package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.DbProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CallMeDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public CallMeDao(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, Logger logger) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public List<CallMe> getAll() {
        List<CallMe> ret = new ArrayList<>();
        String sql = "SELECT C.* FROM " + dbPrefix + "call_me C";
        try ( Statement statement = dbProvider.getConnection().createStatement() ;
              ResultSet resultSet = statement.executeQuery( sql ) ) {
            while( resultSet.next() ) {
                ret.add( new CallMe( resultSet ) );
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " --- " + sql);
        }
        return ret;
    }

    public boolean updateCallMoment(CallMe item){
        if(item == null || item.getId() == null){
            return false;
        }
        String sql = "SELECT CURRENT_TIMESTAMP";
        Timestamp moment;
        try(Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            moment = resultSet.getTimestamp(1);
        }
        catch (SQLException ex){
            logger.log(Level.WARNING,ex.getMessage() + "----" + sql);
            return false;
        }
        sql = "UPDATE " + dbPrefix + "call_me SET call_moment = ? WHERE id = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setTimestamp(1,moment);
            prep.setString(2, item.getId());
            prep.executeUpdate();
            item.setCallMoment(new Date(moment.getTime()));
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.WARNING,ex.getMessage() + "----" + sql);
        }
        return false;
    }

    public CallMe getById(String id){
        if(id == null) {
            return null;
        }

        String sql = "SELECT C.* FROM " + dbPrefix + "call_me C WHERE C.id = ?";

        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1,id);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                return new CallMe(resultSet);
            }
        }
        catch (SQLException ex){
            logger.log(Level.WARNING,ex.getMessage() + "----" + sql);
        }

        return null;
    }
}
/*
DAO - Data Access Object - елементи DAL (Layer) - об'єкти, призначені для роботи із сутностями, переведення роботи з БД
до об'єктів Java та їх коллекцій.
 */
