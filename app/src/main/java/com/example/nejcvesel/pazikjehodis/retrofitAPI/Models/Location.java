package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

/**
 * Created by nejcvesel on 08/12/16.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Location implements Serializable{
    private Integer id;
    private String created;
    private String owner;
    private String latitude;
    private String longtitude;
    private String name;
    private String text;
    private String title;
    private String picture;
    private String address;
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
     * The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     * The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     * The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     * The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longtitude
     */
    public String getLongtitude() {
        return longtitude;
    }

    /**
     *
     * @param longtitude
     * The longtitude
     */
    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    /**
     *
     * @return
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     *
     * @param picture
     * The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {return this.address;}
    public void setAddress(String address) {this.address = address;}


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}