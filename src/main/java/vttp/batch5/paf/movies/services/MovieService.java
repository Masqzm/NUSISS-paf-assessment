package vttp.batch5.paf.movies.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import vttp.batch5.paf.movies.models.Director;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;

@Service
public class MovieService {
  @Autowired
  private MySQLMovieRepository mySQLMovieRepo;
  @Autowired
  private MongoMovieRepository mongoMovieRepo;

  // TODO: Task 2
  

  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public List<Director> getProlificDirectors(int count) {
    List<Director> directors = new ArrayList<>();

    // Get info from mongo side
    directors = mongoMovieRepo.getDirectorInfo();

    directors = mySQLMovieRepo.addToDirectorInfo(directors);

    return directors;
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() {

  }

}
