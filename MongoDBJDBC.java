package api;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class MongoDBJDBC {

   public static void main( String[] args ) {
	
      try{
		
         // To connect to mongodb server
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
         MongoClient mongo = new MongoClient(
        		  new MongoClientURI( "mongodb://username:password@localhost:27017/" ));	
         // Now connect to your databases
         MongoDatabase mydatabase = mongoClient.getDatabase("test");
         System.out.println("Connect to database successfully");
         MongoCollection<Document> collection = mydatabase
 				.getCollection("collection_name");
  
 		List<Document> documents = (List<Document>) collection.find().into(
 				new ArrayList<Document>());
  
                for(Document document : documents){
                    System.out.println(document);
                }
    
			
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
   }
}
