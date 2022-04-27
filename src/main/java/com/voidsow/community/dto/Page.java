package com.voidsow.community.dto;

import java.util.LinkedList;

public class Page {
    public long current;
    public long last;
    public LinkedList<Long> pageNos;

    /**
     * @param recordCount 记录总数
     * @param curPage     当前页码
     * @param pageSize    页大小
     * @param pageNeeded  所需页数
     */
    public Page(long recordCount, int curPage, int pageSize, int pageNeeded) {
        this.current = curPage;
        pageNos = new LinkedList<>();
        //总页数，除不尽还要加1
        long pageNum = recordCount / pageSize;
        if (recordCount % pageSize != 0) {
            pageNum += 1;
        }
        last = pageNum;
        //左右两边的分页栏长度，不能超过到两端的距离
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
