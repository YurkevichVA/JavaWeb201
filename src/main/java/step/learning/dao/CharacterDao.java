package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import step.learning.dto.entities.Character;
import step.learning.dto.models.CharacterFormModel;
import step.learning.services.db.DbProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;
    @Inject
    public CharacterDao( DbProvider dbProvider,
                         Logger logger,
                         @Named("db-prefix") String dbPrefix ) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }
    public boolean addFromForm(CharacterFormModel model) {

        String sql = "INSERT INTO " + dbPrefix + "characters (" +
                "`name`, `cost`, `image` )" +
                "VALUES (?,?,?) " ;
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, model.getName());
            prep.setInt(2, model.getCost());
            prep.setString(3, model.getImage());
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.WARNING,ex.getMessage() + "----" + sql);
        }
        return false;
    }

    public List<Character> getAll() {
        List<Character> res = new ArrayList<>();
        String sql = "SELECT C.* FROM " + dbPrefix + "characters C";
        try ( Statement statement = dbProvider.getConnection().createStatement() ;
              ResultSet resultSet = statement.executeQuery( sql ) ) {
            while( resultSet.next() ) {
                res.add( new Character( resultSet ) );
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " --- " + sql);
        }
        return res;
    }

    public List<Character> getAllOwnedBy(Long userId) {
        List<Character> res = new ArrayList<>();
        String sql = "SELECT C.* FROM " + dbPrefix + "characters C " +
                "JOIN " + dbPrefix + "user_characters UC ON C.id = UC.character_id " +
                "WHERE UC.user_id = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setLong(1, userId);
            try (ResultSet resultSet = prep.executeQuery()) {
                while (resultSet.next()) {
                    res.add(new Character(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " --- " + sql);
        }
        return res;
    }

    public List<Character> getAllExceptOwned(Long userId) {
        List<Character> res = new ArrayList<>();
        String sql = "SELECT C.* FROM " + dbPrefix + "characters C " +
                "WHERE C.id NOT IN (SELECT UC.character_id FROM " + dbPrefix + "user_characters UC WHERE UC.user_id = ?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setLong(1, userId);
            try (ResultSet resultSet = prep.executeQuery()) {
                while (resultSet.next()) {
                    res.add(new Character(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " --- " + sql);
        }
        return res;
    }

    public boolean install() {
        String sql = "CREATE TABLE IF NOT EXISTS " + dbPrefix + "characters (" +
                "`id` BIGINT UNSIGNED PRIMARY KEY DEFAULT ( UUID_SHORT() )," +
                "`name` VARCHAR(64) NOT NULL," +
                "`cost` INTEGER NOT NULL," +
                "`image` VARCHAR(64) NULL" +
                ") ENGINE = INNODB, DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";
        try(Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            return true;
        }
        catch (SQLException ex){
            logger.log(Level.WARNING,ex.getMessage() + "----" + sql);
        }
        return false;
    }
}
