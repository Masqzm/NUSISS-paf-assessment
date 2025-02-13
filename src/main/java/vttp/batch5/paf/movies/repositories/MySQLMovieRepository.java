package vttp.batch5.paf.movies.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.IMDB;
import vttp.batch5.paf.movies.models.exception.SQLInsertionException;

@Repository
public class MySQLMovieRepository {
  @Autowired
  private JdbcTemplate template;

  public static final String SQL_COUNT_IMDB_ROWS = "SELECT count(*) FROM imdb";
  public static final String SQL_GET_IMDB_BYID = "SELECT * FROM imdb WHERE imdb_id = ?";
  public static final String SQL_INSERTBATCH_IMDB = "INSERT INTO imdb(imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime) VALUES (?, ?, ?, ?, ?, ?, ?)";

  public boolean hasLoadedData() {
    // TODO: Might need to check if all data has been loaded instead of checking for
    // empty db
    boolean dataLoaded = false;

    Optional<Integer> countOpt = Optional.ofNullable(template.queryForObject(SQL_COUNT_IMDB_ROWS, Integer.class));

    if (!countOpt.isEmpty())
      dataLoaded = countOpt.get() > 0;

    return dataLoaded;
  }

  public Optional<IMDB> getImdbById(String imdb_id) {
      SqlRowSet rs = template.queryForRowSet(SQL_GET_IMDB_BYID, imdb_id);
      
      if (!rs.next())
        return Optional.empty();

      IMDB imdb = new IMDB(rs.getString("imdb_id"),
                          rs.getDouble("vote_average"),
                          rs.getInt("vote_count"),
                          rs.getString("release_date"),
                          rs.getDouble("revenue"),
                          rs.getDouble("budget"),
                          rs.getInt("runtime"));

      return Optional.of(imdb);
  }

  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<IMDB> imdbs) throws SQLInsertionException {
    List<Object[]> data = imdbs.stream()
                          .map(imdb -> new Object[]{
                            imdb.getImdb_id(),
                            imdb.getVote_average(),
                            imdb.getVote_count(),
                            imdb.getRelease_date(),
                            imdb.getRevenue(),
                            imdb.getBudget(),
                            imdb.getRuntime()
                          })
                          .collect(Collectors.toList());

    template.batchUpdate(SQL_INSERTBATCH_IMDB, data);
  }

  // TODO: Task 3

}
