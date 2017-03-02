package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

/**
 * Created by nejcvesel on 09/12/16.
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private Integer id;
    private String username;
    private List<Integer> locations = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The locations
     */
    public List<Integer> getLocations() {
        return locations;
    }

    /**
     *
     * @param locations
     * The locations
     */
    public void setLocations(List<Integer> locations) {
        this.locations = locations;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}