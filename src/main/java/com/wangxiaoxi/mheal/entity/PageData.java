package com.wangxiaoxi.mheal.entity;

import org.thymeleaf.expression.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-08 17:00
 **/
public class PageData<T> implements Serializable{
    /** 数据集合 */
    protected List<T> result = new ArrayList();
    /** 数据总数 */
    protected int totalCount = 0;
    /** 总页数 */
    protected long pageCount = 0;
    /** 每页记录 */
    protected int pageSize = 5;
    /** 初始当前页 */
    protected int pageNo = 1;
    /**当前遍历问题集合的下标,默认到尾元素*/
    protected int indexEnd = -1;

    public int getIndexEnd() {
        return indexEnd;
    }

    public void setIndexEnd(int indexEnd) {
        this.indexEnd = indexEnd;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "result=" + result +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", indexEnd=" + indexEnd +
                '}';
    }
}