package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class Character {
    // region fields
    private String id;
    private String name;
    private Integer cost;
    private String image;
    // endregion

    // region accessors
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    //endregion

    public Character(ResultSet resultSet) throws SQLException {
        this.setId    ( resultSet.getString ("id"   ) );
        this.setName  ( resultSet.getString ("name" ) );
        this.setCost  ( resultSet.getInt    ("cost" ) );
        this.setImage ( resultSet.getString ("image") );
    }
}
