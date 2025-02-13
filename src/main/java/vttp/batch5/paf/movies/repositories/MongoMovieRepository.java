package vttp.batch5.paf.movies.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Repository;

import com.mongodb.DuplicateKeyException;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.batch5.paf.movies.models.Director;
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
                                    d.put("directors", d.getString("director"));    // rename field (qn requirements)
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
    /*
        db.imdb.aggregate([
            // Project important fields and split directors str into array
            { 
                $project: 
                { 
                    directors: { $split: ["$directors", ", "] },
                    _id: 0
                } 
            },
            { $unwind: '$directors' },
            {
                $group: {
                    _id: '$directors', 
                    imdb_ids: { $push: '$imdb_id' },
                    count: { $sum: 1 }
                }
            },
            { $sort: { count: -1 } }
        ])
     */
    public List<Director> getDirectorInfo() {
        
        ProjectionOperation projectFields = Aggregation.project("directors")
                                            .andExpression("{$split: ['$directors', ', ']}").as("directors")
                                            .andExclude("_id");
        
                                            UnwindOperation unwindDirectors = Aggregation.unwind("directors");
        
        GroupOperation groupByDirector = Aggregation.group("directors")
                                        .push("imdb_id").as("imdb_ids")
                                        .count().as("count");
        
        SortOperation sortDesc = Aggregation.sort(Sort.Direction.DESC, "count");

        Aggregation pipeline = Aggregation.newAggregation(projectFields, unwindDirectors, groupByDirector, sortDesc);

        List<Document> results = template.aggregate(pipeline, COLLECTION_NAME, Document.class).getMappedResults();

        List<Director> directors = new ArrayList<>();

        for (Document doc : results) {

            Director dir = new Director();

            dir.setName(doc.getString("_id"));

            List<IMDB> imdbs = new ArrayList<>();

            for (String imdb_id : doc.getList("imdb_ids", String.class)) {
                IMDB imdb = new IMDB(); 

                imdb.setImdb_id(imdb_id);

                imdbs.add(imdb);
            }

            dir.setImdbs(imdbs);            
        }

        return directors;
    }

}
