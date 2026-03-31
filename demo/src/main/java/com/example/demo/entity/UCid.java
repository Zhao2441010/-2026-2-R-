package com.example.demo.entity;


import jakarta.persistence.*;

@Embeddable//定义嵌入主键
public class UCid implements java.io.Serializable {
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "cat_id")
    private Long catId;

    

    public UCid() {
    }

    public UCid( Long userId,Long catId) {
        this.catId = catId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UCid ucid = (UCid) o;
        return catId.equals(ucid.catId) && userId.equals(ucid.userId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(catId, userId);
    }

    public Long getCatid() {
        return catId;
    }

    public void setCatid(Long catId) {
        this.catId = catId;
    }

    public Long getUserid() {
        return userId;
    }

    public void setUserid(Long userId) {
        this.userId = userId;
    }

}
