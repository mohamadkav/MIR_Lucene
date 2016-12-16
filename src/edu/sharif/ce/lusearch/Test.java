package edu.sharif.ce.lusearch;

import org.apache.lucene.document.Document;

import java.util.List;

/**
 * Created by mohammad on 12/17/16.
 */
public class Test {
    public static void main(String[] args) {
        IndexManager indexManager=new IndexManager("/home/mohammad/Desktop/indexTest");
        indexManager.initIndex("/home/mohammad/IdeaProjects/MIR_Phase2/resources/PersianPoemsData/Poems");

        QueryManager queryManager=new QueryManager();
        try {
            List<Document>documents=queryManager.search(null, "پوزبندی", null, indexManager.generateReader());
            for(Document document:documents){
                System.out.println(document.toString());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
