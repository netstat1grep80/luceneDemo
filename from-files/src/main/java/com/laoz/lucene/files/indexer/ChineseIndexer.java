package com.laoz.lucene.files.indexer;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: laoz
 * @Time: 2022/8/2  15:15
 * @description:
 */
@Slf4j
public class ChineseIndexer {  /**
 * 存放索引的位置
 */
private Directory dir;
    /**
     * 生成索引
     * @param indexDir
     * @throws Exception
     */
    public void index(String indexDir,String dataDir) throws Exception {
        dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriter writer = getWriter();
        File[] files = new File(dataDir).listFiles();

        for(File file : files) {
            Document doc = new Document();
            StringBuilder content = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){
                content.append(line);
            }
            writer.deleteDocuments(new Term("title",file.getName()));
            doc.add(new StringField("title",file.getName(),Field.Store.YES));
            doc.add(new TextField("content", content.toString(),Field.Store.YES));
            writer.addDocument(doc);
        }
        //close了才真正写到文档中
        writer.commit();
        writer.close();
    }


    /**
     * 获取IndexWriter实例
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception {
        //使用中文分词器
        IKAnalyzer analyzer = new IKAnalyzer(true);
        //将中文分词器配到写索引的配置中
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //实例化写索引对象
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

}
