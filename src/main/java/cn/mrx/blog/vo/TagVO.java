package cn.mrx.blog.vo;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 11:45
 * Description:
 */
public class TagVO {

    private String name;
    private Long count;

    /** 构造器 */
    public TagVO() {
    }

    public TagVO(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    /** getter and setter method */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
