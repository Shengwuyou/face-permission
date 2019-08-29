package com.face.permission.common.responses;

import com.face.permission.common.constants.ReturnConstant;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-08 17:47
 */
@Data
public class ResultInfo<T> implements Serializable {
    /**
     * 响应码
     */
    private Long code;
    /**
     * 响应描述
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static ResultInfo<?> success(Object data){
        ResultInfo resultInfo = new ResultInfo(data);
        return resultInfo;
    }

    public static ResultInfo<?> error(Long code, String message, Object data){
        ResultInfo resultInfo = new ResultInfo(code, message, data);
        return resultInfo;
    }

    public static ResultInfo<?> error(Long code, String message){
        ResultInfo resultInfo = new ResultInfo(code, message);
        return resultInfo;
    }

    public static ResultInfo<?> success(Long code, String message){
        ResultInfo resultInfo = new ResultInfo(code, message);
        return resultInfo;
    }

    public static <T> ResultInfo<PageData<T>> buildPageResult(List<T> list, Integer size) {
        ResultInfo<PageData<T>> commonResult = new ResultInfo<>();
        PageData<T> pageData = new PageData<>();
        if (CollectionUtils.isEmpty(list)) {
            pageData.setList(Collections.emptyList());
            // 设置最后一页
            pageData.setEndPage(true);
        } else {
            pageData.setList(list);
            if (list.size() < size) {
                pageData.setEndPage(true);
            }
        }
        commonResult.setData(pageData);
        return commonResult;
    }

    public static <T> ResultInfo<PaginatedResultData<T>> buildPaginatedResult(PageQuery query, List<T> list, Integer total) {
        ResultInfo<PaginatedResultData<T>> commonResult = new ResultInfo<>();
        PaginatedResultData<T> paginatedResultData = new PaginatedResultData<>();
        if (CollectionUtils.isEmpty(list)) {
            paginatedResultData.setList(Collections.emptyList());
        } else {
            paginatedResultData.setList(list);
        }
        Integer totalPage = 0;
        Integer size = query.getSize();
        if (size != 0) {
            totalPage = total / size;
            if (total != 0 && total % size != 0) {
                totalPage++;
            }
        }
        paginatedResultData.setTotalCount(total);
        paginatedResultData.setCurrentPage(query.getPage());
        paginatedResultData.setTotalPage(totalPage);
        commonResult.setData(paginatedResultData);
        return commonResult;
    }

    public static <T> ResultInfo<PageData<T>> buildPageResult(PageData<T> pageData) {
        ResultInfo<PageData<T>> commonResult = new ResultInfo<>();
        commonResult.setData(pageData);
        return commonResult;
    }


    /**
     * 请求成功，不含data的构造函数
     */
    public ResultInfo() {
        this.code = ReturnConstant.SUCCESS_CODE;
        this.message = ReturnConstant.SUCCESS_MSG;
        this.data = null;
    }

    /**
     * 请求成功，带data的构造函数
     * @param data
     */
    public ResultInfo(T data) {
        this.code = ReturnConstant.SUCCESS_CODE;
        this.message = ReturnConstant.SUCCESS_MSG;
        this.data = data;
    }

    /**
     * 需要设置code和 msg的构造函数，一般为异常返回
     * @param code
     * @param message
     */
    public ResultInfo(Long code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * code，message，data都需要自定义的返回设置
     * @param code
     * @param message
     * @param data
     */
    public ResultInfo(Long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
