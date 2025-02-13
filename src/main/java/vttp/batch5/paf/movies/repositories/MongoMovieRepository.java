package vttp.batch5.paf.movies.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.DuplicateKeyException;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.batch5.paf.movies.models.IMDB;
import vttp.batch5.paf.movies.models.exception.SQLErrorLog;

@Repository
public class MongoMovieRepository {
    @Autowired
	private MongoTemplate template;

    public static final String COLLECTION_NAME = "imdb";

    // TODO: Task 2.3
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    //    native MongoDB query here
    /*
        db.imdb.createIndex(
            { imdb_id: 1 },
            { unique: true }
        )
        db.imdb.insertMany([
            <doc>,
            <doc>,
            ...
        ])
     */
    public void batchInsertMovies(List<String> imdbsJSON) {      
        try {  
            IndexOptions indexOptions = new IndexOptions().unique(true);
            template.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending("imdb_id"), indexOptions);

            // Process docs, remove unneeded fields
            List<Document> docs = imdbsJSON.stream()
                                .map(j -> Document.parse(j.toString()))
                                .map(d ->  {                          
                                    d.remove("vote_average");        
                                    d.remove("vote_count");        
                                    d.remove("status");        
                                    d.remove("release_date");        
                                    d.remove("revenue");        
                                    d.remove("runtime");   
                                    d.remove("budget");
                                    d.remove("original_language");
                                    d.remove("popularity");
                                    d.remove("spoken_languages");
                                    d.remove("casts");
                                    d.remove("poster_path");
                                    return d;
                                })
                                .collect(Collectors.toList());

            template.insert(docs, COLLECTION_NAME);
        } catch (DuplicateKeyException ex) {
            System.out.println("Duplicate imdb_id values encountered, couldn't create index");
        }
    }

    // TODO: Task 2.4
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    //    native MongoDB query here
    //
    /*
        db.errors.insert({
            ids: [errorLog.getIds()],
            error: errorLog.getError(),
            timestamp: errorLog.getDate()
        })
     */
    public void logError(SQLErrorLog errorLog) {
        JsonArrayBuilder jArrB = Json.createArrayBuilder();
        for (String id : errorLog.getIds()) 
            jArrB.add(id);   
        
        Document doc = Document.parse(Json.createObjectBuilder()
                                    .add("ids", jArrB)
                                    .add("error", errorLog.getError())
                                    .add("timestamp", errorLog.getDate().toString())
                                    .build().toString());
        
        template.insert(doc, COLLECTION_NAME);
    }

    // TODO: Task 3
    // Write the native Mongo query you implement in the method in the comments
    //
    //    native MongoDB query here
    //


}
