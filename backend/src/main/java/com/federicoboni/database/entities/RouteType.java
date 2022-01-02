package com.federicoboni.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "types")
public class RouteType {

    @Id
    @Column(name = "id")
    protected int id;

    @Column(name = "type")
    protected String type;

    public RouteType() {
    }

    public RouteType(String type) {
        super();
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public void setUsername(String type) {
        this.type = type;
    }

}
