package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

/**
 * Created by nejcvesel on 11/02/17.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {

    private Integer id;
    private String owner;
    private String created;
    private String name;
    private String city;
    private String description;
    private List<Integer> pathLocations = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description; }

    public List<Integer> getPathLocations() {
        return pathLocations;
    }

    public void setPathLocations(List<Integer> pathlocations) {
        this.pathLocations = pathlocations;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}