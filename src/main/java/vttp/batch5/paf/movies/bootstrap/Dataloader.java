package vttp.batch5.paf.movies.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.paf.movies.models.IMDB;
import vttp.batch5.paf.movies.models.exception.SQLErrorLog;
import vttp.batch5.paf.movies.models.exception.SQLInsertionException;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Component
public class Dataloader implements CommandLineRunner {
  @Autowired
  private MySQLMovieRepository sqlMovieRepo;
  @Autowired
  private MongoMovieRepository mongoMovieRepo;

  @Value("${data.zip.path}")
  private String dataZipFilePath;   // data zipped file path 

  public static final int BATCH_INSERT_MAX = 25;

  //TODO: Task 2
  @Override
  public void run(String... args) throws Exception {
    // Skip data loading if it has already been loaded 
    //if(sqlMovieRepo.hasLoadedData())
    //  return;
    
    ZipFile zipFile = new ZipFile(dataZipFilePath);
    
    if(!zipFile.entries().hasMoreElements()) {
      System.out.println("JSON file cant be found inside zip file. Possible loading failure");
      zipFile.close();
      return;
    }
    
    // Get file in zip
    ZipEntry entry = zipFile.entries().nextElement();

    // Read file
    InputStream stream = zipFile.getInputStream(entry);
    InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
    Scanner inputStream = new Scanner(reader);

    List<IMDB> imdbs = new ArrayList<>();             // for MySQL batch insert
    List<String> imdbsJSONStr = new ArrayList<>();    // for MongoDB batch insert

    while (inputStream.hasNext()) {
      // Get line by line (each line is 1 jsonObject)
      String json = inputStream.nextLine(); 

      // Skip null or non json objects
      if(json == null || !(json.startsWith("{") && json.endsWith("}")))
        continue;

      JsonReader jReader = Json.createReader(new StringReader(json));
      JsonObject j = jReader.readObject();
      
      // Filter movies >= 2018
      String dateStr = j.getString("release_date");
      
      // Skip movies with no date
      if(dateStr.isBlank())
        continue;

      LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

      // Add to list if movie released >= 2018
      if(date.getYear() >= 2018) {
        IMDB imdb = IMDB.jsonToIMDB(json);
        
        // Skip data loading process if data has been loaded before
        if(!sqlMovieRepo.getImdbById(imdb.getImdb_id()).isEmpty())
        {
          System.out.println("Movies already loaded into db. Skipping dataloader...");
          return;
        }

        if(imdbs.size() < BATCH_INSERT_MAX) {
          imdbs.add(imdb);
          imdbsJSONStr.add(json);          
        } else {
          try {
            sqlMovieRepo.batchInsertMovies(imdbs);
            
            mongoMovieRepo.batchInsertMovies(imdbsJSONStr);
          } catch(Exception ex) {
            // Get list of ids of failed IMDBs insertion
            List<String> ids = new ArrayList<>();
            for (IMDB i : imdbs) 
              ids.add(i.getImdb_id());

            SQLErrorLog sqlErrorLog = new SQLErrorLog(ids.toArray(new String[0]), ex.getMessage(), new Date());

            // Log error into mongodb
            mongoMovieRepo.logError(sqlErrorLog);
          }
          

          // Reset list so we can do batch insert again
          imdbs.clear();
          imdbsJSONStr.clear();
        }
      }
    }
    System.out.println("Data loading complete");

    inputStream.close();
    stream.close();
    zipFile.close();
  }
}
