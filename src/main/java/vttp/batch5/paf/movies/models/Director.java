package vttp.batch5.paf.movies.models;

import java.util.List;

public class Director {
    private String name;
    private List<IMDB> imdbs;
    private double total_revenue;
    private double total_budget;

    public Director() {}
    public Director(String name, List<IMDB> imdbs, double total_revenue, double total_budget) {
        this.name = name;
        this.imdbs = imdbs;
        this.total_revenue = total_revenue;
        this.total_budget = total_budget;
    }

    @Override
    public String toString() {
        return "Director [name=" + name + ", imdbs=" + imdbs + ", total_revenue=" + total_revenue + ", total_budget="
                + total_budget + "]";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<IMDB> getImdbs() {
        return imdbs;
    }
    public void setImdbs(List<IMDB> imdbs) {
        this.imdbs = imdbs;
    }
    public double getTotal_revenue() {
        return total_revenue;
    }
    public void setTotal_revenue(double total_revenue) {
        this.total_revenue = total_revenue;
    }
    public double getTotal_budget() {
        return total_budget;
    }
    public void setTotal_budget(double total_budget) {
        this.total_budget = total_budget;
    }

    
}
