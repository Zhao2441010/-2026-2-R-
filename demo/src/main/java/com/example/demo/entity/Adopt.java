package com.example.demo.entity;


import jakarta.persistence.*;
import java.util.Date;


import com.example.demo.entity.UCid;
import com.example.demo.entity.User;
import com.example.demo.entity.Cat;


@Entity
@Table(name = "adopt")
public class Adopt {
    
    @EmbeddedId
    private UCid uc;

    @ManyToOne(fetch = FetchType.LAZY)
    //lazy加载，只有在真正需要使用user对象时才会去数据库中查询对应的user数据，避免了不必要的数据库查询，提高了性能
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", insertable = false, updatable = false)
    private Cat cat;
//这里的private不会在数据库中添加user除了id的信息



    @Column(name = "adopt_date")
    @Temporal(TemporalType.DATE)
    //注解存储精度 date日期, time时间, timestamp日期和时间
    private Date adoptDate;

    @Column(name="process")
    private String process;

    public enum Process{
        ACCEPTED, REJECTED, PENDING,GIVEN_UP,ABANDONED
    }

    protected Adopt() {
    }

    public Adopt( Long userId, Long catId, Date adoptDate, String process) {
        this.uc = new UCid(userId,catId);
        this.adoptDate = adoptDate;
        this.process = process;
    }

    public Long getUserId() {
        return uc.getUserid();
    }

    public Long getCatId() {
        return uc.getCatid();
    }

    public Date getAdoptDate() {
        return adoptDate;
    }

    public String getProcess() {
        return process;
    }

    public void setAdoptDate(Date adoptDate) {
        this.adoptDate = adoptDate;
    }

    public void setProcess(String process) {
        this.process = process;
    }

}
