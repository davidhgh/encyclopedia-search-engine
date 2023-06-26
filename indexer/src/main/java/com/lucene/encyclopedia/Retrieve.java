package com.lucene.encyclopedia;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import java.nio.file.Paths;
public class Retrieve {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println( "Hello World! + test.java" );
        
        
        
        String indexPath = "C:\\Users\\junji\\Desktop\\jajaja\\lucene\\indexs";

        try {
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            IndexReader reader = DirectoryReader.open(directory);

            // Perform search or retrieve documents using the reader
            myMethod(reader);
            
            
            reader.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
  static void myMethod(IndexReader reader) {

	    try {
		    int numDocs = reader.numDocs();

		    for (int i = 0; i < numDocs; i++) {
		        Document doc = reader.document(i);
		        
		        // Access and process the document fields, Case-sensitive
		        String id = doc.get("url");
		        String title = doc.get("title");
		        String content = doc.get("description");
		        
		        // Perform further processing with the document
		        System.out.println("URL: " + id);
		        System.out.println("Title: " + title);
		        System.out.println("Description: " + content);
		        System.out.println("-------------------------------------");
		    }
		} catch (Exception e) {
            e.printStackTrace();
		}

  	}
  
  
  
  
  
  
  
}
