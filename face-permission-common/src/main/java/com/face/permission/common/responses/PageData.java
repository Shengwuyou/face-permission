package com.face.permission.common.responses;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author plw on 2018/4/25 上午11:39.
 * @version 1.0
 */
public class PageData<T> {

    @ApiModelProperty(value = "列表")
    private List<T> list;

    @ApiModelProperty(value = "是否为最后一页")
    private boolean endPage;

    public PageData() {
    }

    public PageData(List<T> list, boolean endPage) {
        this.list = list;
        this.endPage = endPage;
    }
    
    public PageData(List<T> list, int pageSize) {
        this.list = list;
        if(CollectionUtils.isEmpty(list)){
        	 this.endPage = true;
        }else{
        	 this.endPage =list.size()<pageSize;
        }
       
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isEndPage() {
        return endPage;
    }

    public void setEndPage(boolean endPage) {
        this.endPage = endPage;
    }
}
