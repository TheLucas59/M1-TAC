package com.ulille.mmolist.api.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GameDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("game_url")
    @Expose
    private String gameUrl;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("developer")
    @Expose
    private String developer;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @Ignore
    @SerializedName("minimum_system_requirements")
    @Expose
    private MinimumSystemRequirements minimumSystemRequirements;
    @SerializedName("screenshots")
    @Expose
    private List<Screenshot> screenshots = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getMinimumSystemRequirements() {
        if(minimumSystemRequirements != null) {
            return minimumSystemRequirements.toString();
        }else{
            return "Pas d'informations";
        }
    }

    public void setMinimumSystemRequirements(MinimumSystemRequirements minimumSystemRequirements) {
        this.minimumSystemRequirements = minimumSystemRequirements;
    }

    public List<Screenshot> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<Screenshot> screenshots) {
        this.screenshots = screenshots;
    }

    public static Game getGameFromGameDetails(GameDetails gameDetails) {
        Game g = new Game();
        g.setId(gameDetails.getId());
        g.setTitle(gameDetails.getTitle());
        g.setThumbnail(gameDetails.getThumbnail());
        g.setShortDescription(gameDetails.getShortDescription());
        g.setGameUrl(gameDetails.getGameUrl());
        g.setGenre(gameDetails.getGenre());
        g.setPlatform(gameDetails.getPlatform());
        g.setPublisher(gameDetails.getPublisher());
        g.setDeveloper(gameDetails.getDeveloper());
        g.setReleaseDate(gameDetails.getReleaseDate());
        g.setProfileUrl(gameDetails.getProfileUrl());
        return g;
    }

}