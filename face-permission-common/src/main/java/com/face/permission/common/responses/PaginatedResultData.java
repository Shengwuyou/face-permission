package com.face.permission.common.responses;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by sunyu on 2018/4/19
 */
public class PaginatedResultData<T> {

    @ApiModelProperty(value = "当前页")
    private int currentPage = 0;

    @ApiModelProperty(value = "总页码")
    private int totalPage = 0;

    @ApiModelProperty(value = "总条目数")
    private int totalCount;

    @ApiModelProperty(value = "列表")
    private List<T> list;

    public PaginatedResultData() {
        super();
    }


    /**
     * Constructor Method
     *
     * @param currentPage
     * @param totalPage
     * @param list
     */
    public PaginatedResultData(int currentPage, int totalPage, int totalCount, List<T> list) {
        super();
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.list = list;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
