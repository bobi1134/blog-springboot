package cn.mrx.blog.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 0:36
 * Description:
 */
@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    /** 点赞-用户：一对一 */
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    /** 构造器 */
    public Vote() {
    }

    public Vote(User user) {
        this.user = user;
    }

    /** getter and setter method */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
