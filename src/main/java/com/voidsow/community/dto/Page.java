package com.voidsow.community.dto;

import java.util.LinkedList;

public class Page {
    public long current;
    public long last;
    public LinkedList<Long> pageNos;

    public Page(long recordCount, int curPage, int pageSize, int pageNeeded) {
        this.current = curPage;
        pageNos = new LinkedList<>();
        long pageNum = recordCount / pageSize;
        if (recordCount % pageSize != 0) {
            pageNum += 1;
        }
        last = pageNum;
        var left = Math.min(pageNeeded / 2, current - 1);
        var right = Math.min(pageNeeded / 2, pageNum - current);
        //除掉page自身
        pageNeeded -= 1;
        //为远离尾端的一半加上多余的页码
        if (current <= pageNum / 2)
            right += pageNeeded - left - right;
        else
            left += pageNeeded - left - right;
        //page本身在左边添加
        for (int i = 0; i <= left; i++)
            pageNos.addFirst(current - i);
        for (int i = 1; i <= right; i++)
            pageNos.addLast(current + i);
    }
}
