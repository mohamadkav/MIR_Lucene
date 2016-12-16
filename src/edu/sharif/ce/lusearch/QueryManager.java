package edu.sharif.ce.lusearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad on 12/17/16.
 */
public class QueryManager {

    public List<Document> search(String poet, String body, String title, IndexReader indexReader) throws ParseException,IOException{
        List<Document>toReturn=new ArrayList<>();
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        BooleanQuery.Builder queryBuilder=new BooleanQuery.Builder();
        if(body!=null)
            queryBuilder.add(new QueryParser("CONTENTS",new StandardAnalyzer()).parse(body), BooleanClause.Occur.MUST);
        if(poet!=null)
            queryBuilder.add(new QueryParser("POET",new StandardAnalyzer()).parse(poet), BooleanClause.Occur.MUST);
        if(title!=null)
            queryBuilder.add(new QueryParser("TITLE",new StandardAnalyzer()).parse(title), BooleanClause.Occur.MUST);
        TopDocs hits=indexSearcher.search(queryBuilder.build(),10);
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            toReturn.add(doc);
        }
        indexReader.close();
        return toReturn;
    }
}
