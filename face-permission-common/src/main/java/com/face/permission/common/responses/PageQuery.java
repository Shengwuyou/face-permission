package com.face.permission.common.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by sunyu on 2018/4/28
 */
@ApiModel(description = "分页查询")
public class PageQuery {

    /**
     * 每页默认显示的记录数大小: 10条
     */
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 每页最大显示的记录数大小: 600条
     */
    private static final Integer MAX_PAGE_SIZE = 600;

    /**
     * 每页显示的记录条数
     */
    @ApiParam(value = "每页大小，最大20", defaultValue = "10")
    private Integer size = DEFAULT_PAGE_SIZE;

    /**
     * 当前页数
     */
    @ApiParam(defaultValue = "0", value = "当前页码")
    private Integer page = 0;

    /**
     * 当前页数
     */
    @ApiParam(value = "前一页的最后一条记录id,如果没有可以不传")
    private Long lastId;

    @ApiParam(value = "id  0:desc   / 1:asc")
    private Integer orderBy;
    // *************************************************************************//
    // ************************* 以下为Getter/Setter方法 ************************//
    // ************************************************************************//
    @ApiIgnore
    public Integer getStartRow() {
        if (page < 0) {
            page = 0;
        }
        // 使用了lastId就不能使用startRow
        return Objects.nonNull(lastId) && lastId > 0 ? 0 : (page * size);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = Optional.ofNullable(size)
                .filter(pageSize -> pageSize <= MAX_PAGE_SIZE)
                .orElse(DEFAULT_PAGE_SIZE);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = Optional.ofNullable(page).orElse(1);
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        // lastId不能初始为0
        if (Objects.nonNull(lastId) && lastId > 0) {
            this.lastId = lastId;
        }
    }

    public Integer getOrderBy() {
        if (orderBy == 0){
            return 0;
        }
        return 1;
    }

}
