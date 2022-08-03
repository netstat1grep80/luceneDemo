package com.laoz.lucene.files.search;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: laoz
 * @Time: 2022/8/2  15:54
 * @description:
 */
public class ChineseSearch {

    private static final Logger logger = LoggerFactory.getLogger(ChineseSearch.class);
    private static String highLightPre = "<font color=red><b>";
    private static String highLightSuf = "</b></font>";
    private static Pattern MY_PATTERN = Pattern.compile("<font color=red><b>(.*?)<\\/b><\\/font>");
    public static List<String> search(String indexDir, String q) throws Exception {

        //获取要查询的路径，也就是索引所在的位置
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);


        //使用中文分词器
        IKAnalyzer analyzer = new IKAnalyzer(true);
         /*
        //由中文分词器初始化查询解析器
        QueryParser parser = new QueryParser("content", analyzer);
        //通过解析要查询的String，获取查询对象
        Query query = parser.parse(q);
         */

        String[] fields = {"title", "content"};
        MultiFieldQueryParser multiFieldQuery = new MultiFieldQueryParser(fields, analyzer);
        Query query = multiFieldQuery.parse(q);

        //记录索引开始时间
        long startTime = System.currentTimeMillis();
        //开始查询，查询前10条数据，将记录保存在docs中
        TopDocs docs = searcher.search(query, 100);
        //记录索引结束时间
        long endTime = System.currentTimeMillis();
        logger.info("匹配{}共耗时{}毫秒", q, (endTime - startTime));
        logger.info("查询到{}条记录", docs.totalHits);

        //如果不指定参数的话，默认是加粗，即<b><b/>
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(highLightPre,highLightSuf);
        //根据查询对象计算得分，会初始化一个查询结果最高的得分
        QueryScorer scorer = new QueryScorer(query);
        //根据这个得分计算出一个片段
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        //将这个片段中的关键字用上面初始化好的高亮格式高亮
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        //设置一下要显示的片段
        highlighter.setTextFragmenter(fragmenter);
        //取出每条查询结果
        List<String> list = new ArrayList<>();
        for(ScoreDoc scoreDoc : docs.scoreDocs) {
            logger.info(scoreDoc.toString());
            //scoreDoc.doc相当于docID,根据这个docID来获取文档
            Document doc = searcher.doc(scoreDoc.doc);
            String content = doc.get("content");
//            logger.info(content);
            String title = doc.get("title");

            //显示高亮

            if(content != null) {
                StringReader stringReader = new StringReader(content);
                TokenStream tokenStream = analyzer.tokenStream("content", stringReader);
                String[] summary = highlighter.getBestFragments(tokenStream,content,10);
                String body = String.join("<br />",summary);
                body = moreWord(body);
                if(!body.equals("")) {
                    list.add("<font size=3>" + title + "</font><br /><i><font size=2 color=gray>" + body + "</font></i>");
                }
            }

        }
        return list;
    }

    /**
     * 单个字的分词就不再显示了
     * @param body
     * @return
     */
    private static String moreWord(String body){
        Matcher m = MY_PATTERN.matcher(body);
        Boolean add = false;
        while(m.find()){
            if(m.group(1).length() > 1){
                add = true;
                break;
            }else{
//                logger.info("body.replace({},{})",highLightPre+m.group(1)+highLightSuf,m.group(1));
//                body = body.replaceAll(highLightPre+m.group(1)+highLightSuf,m.group(1));
            }
        }

        if(!add){
            return "";
        }

        return body;
    }
}