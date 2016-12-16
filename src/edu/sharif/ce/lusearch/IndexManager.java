package edu.sharif.ce.lusearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;

/**
 * Created by mohammad on 12/17/16.
 */
public class IndexManager {
    private FileWriter fileWriter;
    private IndexWriter writer;

    public void initIndex(String directoryPath){
        File file=new File(directoryPath);//Paths.get(directoryPath).getFileName().toFile();
        if(!file.exists()||!file.isDirectory())
            throw new NullPointerException("Nope. not the right path");
        processFiles(file.listFiles());
        try{
            writer.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public IndexManager(String indexSavePath) {
        try {
            Directory storeDirectory = FSDirectory.open(Paths.get(indexSavePath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(storeDirectory, config);
        }catch (Exception e){
            e.printStackTrace();
            throw new NullPointerException("Problem in index initiation");
        }
    }

    public IndexReader generateReader(){
        try {
            return DirectoryReader.open(writer.getDirectory());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private void processFiles(File[] files){
        for (File file : files) {
            if (file.isDirectory())
                processFiles(file.listFiles()); // Calls same method again.
            else
                parseSingeFile(file);
        }
    }

    private void parseSingeFile(File file){
        Document doc = new Document();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String rawLine;
            StringBuilder rawDoc=new StringBuilder("");
            while ((rawLine = reader.readLine()) != null) {
                if(rawLine.startsWith(Config.META_DELIMITER)){
                    String languageConf=rawLine.substring(rawLine.indexOf(Config.META_DELIMITER)+Config.META_DELIMITER.length(),rawLine.indexOf(":"));
                    String config=Config.CONFIG_MAPPER.get(languageConf);
                    if(languageConf.equals(Config.TEXT_INDICATOR))
                        continue; //Because we'll process all the unformatted text!
                    else if(config==null)
                        throw new NullPointerException("BAD META. BAAAD");
                    doc.add(new TextField(config,reader.readLine(), Field.Store.YES));
                }
                else
                    rawDoc.append(rawLine+"\n");
            }
            doc.add(new TextField("CONTENTS",rawDoc.toString(), Field.Store.YES));
            writer.addDocument(doc);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
