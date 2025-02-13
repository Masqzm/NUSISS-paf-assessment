package vttp.batch5.paf.movies.models;

import java.io.StringReader;
import java.sql.Date;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class IMDB {
    private String imdb_id;
    private double vote_average;
    private int vote_count;
    private String release_date;
    private double revenue;
    private double budget;
    private int runtime;

    public IMDB() {}
    public IMDB(String imdb_id, double vote_average, int vote_count, String release_date, double revenue, double budget,
            int runtime) {
        this.imdb_id = imdb_id;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.release_date = release_date;
        this.revenue = revenue;
        this.budget = budget;
        this.runtime = runtime;
    }

    public static IMDB jsonToIMDB(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        IMDB imdb = new IMDB();
        

        // Try setting fields. If fields are null or erroneous set accordingly to default values
        imdb.setImdb_id(Optional.ofNullable(j.getString("imdb_id")).orElse(""));
        //imdb.setImdb_id(Optional.ofNullable(j.getOrDefault("imdb_id", JsonValue.NULL).toString()).orElse(""));

        imdb.setVote_average(Optional.ofNullable(j.getJsonNumber("vote_average").doubleValue()).orElse(Double.valueOf(0)));
        
        imdb.setVote_count(Optional.ofNullable(j.getInt("vote_count")).orElse(0));
        
        imdb.setRelease_date(j.getString("release_date"));  // will never be null as it is first checked before processing
        
        imdb.setRevenue(Optional.ofNullable(j.getJsonNumber("revenue").doubleValue()).orElse(Double.valueOf(0)));
        
        imdb.setBudget(Optional.ofNullable(j.getJsonNumber("budget").doubleValue()).orElse(Double.valueOf(0)));
        
        imdb.setRuntime(Optional.ofNullable(j.getInt("runtime")).orElse(0));

        // Try setting fields. If fields are null or erroneous they wont be set
        // try {
        //     j.getOrDefault("imdb_id", J);
        //     imdb.setImdb_id();
        // } catch(Exception ex) {}
            
        return imdb;
    }


    public String getImdb_id() {
        return imdb_id;
    }
    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }
    public double getVote_average() {
        return vote_average;
    }
    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
    public int getVote_count() {
        return vote_count;
    }
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public double getRevenue() {
        return revenue;
    }
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    public double getBudget() {
        return budget;
    }
    public void setBudget(double budget) {
        this.budget = budget;
    }
    public int getRuntime() {
        return runtime;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
