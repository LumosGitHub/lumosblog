package lumosblog.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 冠麟
 * @date 2019/9/17 8:41
 * 返回页面
 */
@Data
public class Page<T> {


    public Page(org.springframework.data.domain.Page page) {
        int number = page.getNumber() + 1;
        setPageNum(number);
        setPrevPage(number-1);
        setNextPage(number+1);
        setTotalPages(page.getTotalPages());
        setFirstPage(page.isFirst());
        setLastPage(page.isLast());
        setHasNextPage(page.hasNext());
        setHasPrevPage(page.hasPrevious());
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= page.getTotalPages(); i++) {
            list.add(i);
        }
        Integer[] integers = list.toArray(new Integer[0]);
        setNavPageNums(integers);
    }


    public Page() {
    }

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 限制每页行数
     */
    private Integer limit;

    /**
     * 上一页页码
     */
    private Integer prevPage;

    /**
     * 下一页页码
     */
    private Integer nextPage;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 总行数
     */
    private Integer totalRows;
    private List<T> rows;

    /**
     * 是否第一页
     */
    private boolean isFirstPage;

    /**
     * 是否最后一页
     */
    private boolean isLastPage;

    /**
     * 是否有上一页
     */
    private boolean hasPrevPage;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;
    private Integer navPages;

    /**
     * 所有导航页码
     */
    private Integer[] navPageNums;
    private boolean success;
    private String msg;
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp;

}
