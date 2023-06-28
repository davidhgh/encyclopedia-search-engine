package com.lucene.encyclopedia;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class Read {

	public static void main(String[] args) {
		try {
            // Create a Redis client pointing to your Redis instance
			RedisClient redisClient = RedisClient.create("redis://10.147.19.60:6379");

            long iterations = 30;
            long totalRetrieved = 0; // Total records retrieved
            long steps = 0; // Index to increment for each iteration
            long startIndex = 0; // Start index of the range
            long endIndex = steps;   // End index of the range
            
            // Each iteration uses a separate connection to avoid loading all records in key at once (avoid memory issue)
            for (int i = 0; i < iterations; i++) {
                StatefulRedisConnection<String, String> connection = redisClient.connect();
                connection.setTimeout(Duration.ofMinutes(120));

                // Create a synchronous RedisCommands instance
			    RedisCommands<String, String> redisCommands = connection.sync();

			    // Read a value from Redis using the GET command
			    String key = "geturl:items";
	            Analyzer analyzer = new StandardAnalyzer();
                Long element_len = redisCommands.llen(key);
                steps = Math.floorDiv(element_len, iterations);
                if (i == 0){
                    endIndex = steps - 1;   // account for 0-indexed to avoid indexoutofbounds exception
                }

                List<String> elements = redisCommands.lrange(key, startIndex, endIndex);

                // Save elements to a text file
                String filePath = "elements.txt";
                writetxt(filePath, elements);

                // Create Lucene index writer
                String indexPath = "indexer\\indexes";
                Directory directory = FSDirectory.open(Paths.get(indexPath));
                IndexWriterConfig config = new IndexWriterConfig(analyzer);
                IndexWriter indexWriter = new IndexWriter(directory, config);

                long count = 0;

                // Create Lucene documents for each element and add them to the index
                for (String element : elements) {
                    Document document = createLuceneDocument(element);
                    if (document != null) {
                        indexWriter.addDocument(document);
                    }
                    count = count + 1;
                }

                // Commit changes and close the index writer
                indexWriter.commit();
                indexWriter.close();
                
                totalRetrieved += count;
			    // System.out.println("Retrieved length: " + element_len);
			    System.out.println((i+1) + "/" + iterations + " - Retrieved length: " + totalRetrieved);

                // Increment index to retrieve for next iteration
                startIndex += steps;
                endIndex += steps;
			    
                connection.close();
            }

			// Connect to Redis
			// StatefulRedisConnection<String, String> connection = redisClient.connect();
            // connection.setTimeout(Duration.ofMinutes(120));
			
            // // Create a synchronous RedisCommands instance
			// RedisCommands<String, String> redisCommands = connection.sync();
			// // Read a value from Redis using the GET command
			// String key = "geturl:items";
	        // Analyzer analyzer = new StandardAnalyzer();

			// long index = 0;
            // long startIndex = 0; // Start index of the range
            // long endIndex = 10000;   // End index of the range
			
			// Long element_len = redisCommands.llen(key);
			
            // List<String> elements = redisCommands.lrange(key, startIndex, endIndex);

            // // Save elements to a text file
            // String filePath = "elements.txt";

            // String indexPath = "C:\\Users\\rubyf\\Desktop\\lucene\\indexs";
            
            // // Create Lucene index writer
            // Directory directory = FSDirectory.open(Paths.get(indexPath));
            // IndexWriterConfig config = new IndexWriterConfig(analyzer);
            // IndexWriter indexWriter = new IndexWriter(directory, config);

            // long count = 0;

            // // Create Lucene documents for each element and add them to the index
            // for (String element : elements) {
            //     Document document = createLuceneDocument(element);
            //     if (document != null) {
            //         indexWriter.addDocument(document);
            //     }
            //     count = count + 1;
            // }
            
            // writetxt(filePath, elements);
			
            // // Commit changes and close the index writer
            // indexWriter.commit();
            // indexWriter.close();
            
			// // System.out.println("Retrieved length: " + element_len);
			// System.out.println("Retrieved length: " + count);

			// connection.close();
			redisClient.shutdown();
			
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
	
	public static void writetxt(String txt_name,List<String> elements) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txt_name))) {
            for (String element : elements) {
                writer.write(element);
                writer.newLine();
                System.out.println(element + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		finally {
			// Close the Redis connection and client
			System.out.println("Create Txt file");
		}
	}
	
    private static Document createLuceneDocument(String element) {
        try {
            // Parse JSON element into fields
            JSONObject jsonElement = new JSONObject(element);
            String url = jsonElement.getString("url");
            String title = jsonElement.getString("title");
            String description = jsonElement.getString("description");

            
            System.out.println(url);
            System.out.println(title);
            System.out.println(description);
            
            // Create Lucene document
            Document document = new Document();
            document.add(new Field("url", url, TextField.TYPE_STORED));
            document.add(new Field("title", title, TextField.TYPE_STORED));
            document.add(new Field("description", description, TextField.TYPE_STORED));

            // Add more fields as needed

            return document;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }   
}
