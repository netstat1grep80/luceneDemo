package com.laoz.lucene.files.controller;

import com.laoz.lucene.files.constants.Constants;
import com.laoz.lucene.files.domain.R;
import com.laoz.lucene.files.search.ChineseSearch;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TopDocs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: laoz
 * @Time: 2022/8/2  16:57
 * @description:
 */
@RestController
@Slf4j
public class SearchContrller {
    @GetMapping("/")
    public String search(@RequestParam(value = "q", required = false) String q){
        List<String> list = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        StringBuilder b = new StringBuilder();
        b.append("<h1>四大</h1><form action='http://localhost:8080/'><input type=text name=q value='' style='width:50%;' />&nbsp;&nbsp;<input type=submit value='搜索' /></form><hr />");


        if(q!=null && !q.equals("")) {
            try {
                list = ChineseSearch.search(Constants.INDEX_DIR, q);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            if (list != null) {
                b.append("共查询到<font color=blue>" + list.size() + "</font>条记录，" + "耗时" + (endTime - startTime) + "毫秒<br />");
                int i = 1;
                for (String row : list) {
                    b.append("<br />" + row + "<br /><br />");
                    i++;
                }
            }
        }

        return b.toString();
    }
}
