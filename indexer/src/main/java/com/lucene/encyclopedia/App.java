package com.lucene.encyclopedia;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! + App.java" );
        
        
        try {
            // Step 1: Create a Lucene Directory
            Directory directory = FSDirectory.open(Paths.get("C:\\Users\\junji\\Desktop\\jajaja\\lucene\\indexs"));

            // Step 2: Create an IndexWriter
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);

            // Step 3: Create Documents
            Document doc1 = new Document();
            doc1.add(new Field("ID", "12345678", TextField.TYPE_STORED));
            doc1.add(new Field("title", "Document 1", TextField.TYPE_STORED));
            doc1.add(new Field("content", "This is the content of document 1", TextField.TYPE_STORED));
            
            Document doc2 = new Document();
            doc1.add(new Field("ID", "87654321", TextField.TYPE_STORED));
            doc2.add(new Field("title", "Document 2", TextField.TYPE_STORED));
            doc2.add(new Field("content", "This is the content of document 2", TextField.TYPE_STORED));

            // Step 4: Add Documents to Index
            indexWriter.addDocument(doc1);
            indexWriter.addDocument(doc2);

            // Step 5: Commit or Optimize the Index
            indexWriter.commit(); // or indexWriter.forceMerge(1);

            // Step 6: Close the IndexWriter
            indexWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
