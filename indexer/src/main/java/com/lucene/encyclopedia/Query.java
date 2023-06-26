package com.lucene.encyclopedia;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Query {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        String indexPath = "C:\\Users\\junji\\Desktop\\jajaja\\lucene\\indexs"; // Replace with your index path
        Analyzer analyzer = new StandardAnalyzer();

        // Search query
        String searchQuery = "dictionary"; // Replace with your search query

        // Perform the search
        Directory directory = null;
        IndexReader indexReader = null;

        // Perform the search
        try {
            // Open the index directory
            Path indexPathPath = Paths.get(indexPath);
            directory = FSDirectory.open(indexPathPath);

            // Create the index reader and searcher
            indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // Create a query builder to build the search query
            QueryBuilder queryBuilder = new QueryBuilder(analyzer);
            org.apache.lucene.search.Query query = queryBuilder.createBooleanQuery("description", searchQuery);

            // Execute the search
            TopDocs topDocs = indexSearcher.search(query, 10); // Retrieve top 10 results


            // Process the search results
            System.out.println("Total results found: " + topDocs.totalHits);
            System.out.println("Search results:");
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("Title: " + document.get("title"));
                System.out.println("URL: " + document.get("url"));
                System.out.println("Description: " + document.get("description"));
                System.out.println("-------------");
                // Retrieve and display more fields as needed
            }

            // Close the index reader
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
	}
}
