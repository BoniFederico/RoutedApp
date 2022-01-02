package com.federicoboni.database.entities;

import java.io.Serializable;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routes")
public class Route implements Serializable {
    @Id
    @Column(name = "id")
    protected int id;

    @Column(name = "user")
    protected int user;

    @Column(name = "date")
    protected String date;

    @Column(name = "type")
    protected String type;
    @Column(name = "route")
    protected String route;
    @Column(name = "stages")

    protected String stages;

    public Route() {
    }

    public Route(int id, String route) {
        this.id = id;
        this.route = route;
    }

    public String getStages() {
        return stages;
    }

    public void setStages(String stages) {
        this.stages = stages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
