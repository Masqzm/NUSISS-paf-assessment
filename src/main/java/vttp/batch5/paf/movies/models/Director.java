package vttp.batch5.paf.movies.models;

public class Director {
    private String name;
    private int movies_count;
    private double total_revenue;
    private double total_budget;

    public Director() {
    }
    public Director(String name, int movies_count, double total_revenue, double total_budget) {
        this.name = name;
        this.movies_count = movies_count;
        this.total_revenue = total_revenue;
        this.total_budget = total_budget;
    }

    @Override
    public String toString() {
        return "Director [name=" + name + ", movies_count=" + movies_count + ", total_revenue=" + total_revenue + ", total_budget="
                + total_budget + "]";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMovies() {
        return movies_count;
    }
    public void setMovies_count(int movies_count) {
        this.movies_count = movies_count;
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
