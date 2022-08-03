package com.laoz.lucene.test;

import com.laoz.lucene.files.LuceneFilesApplication;
import com.laoz.lucene.files.indexer.ChineseIndexer;
import com.laoz.lucene.files.search.ChineseSearch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.laoz.lucene.files.constants.Constants.DATA_DIR;
import static com.laoz.lucene.files.constants.Constants.INDEX_DIR;

/**
 * @author: laoz
 * @Time: 2022/8/2  15:26
 * @description:
 */
@Slf4j
@SpringBootTest(classes = LuceneFilesApplication.class)
@RunWith(SpringRunner.class)
public class filesTester {
    @Test
    public void indexerCreate(){
        try {
            new ChineseIndexer().index(INDEX_DIR,DATA_DIR);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
