package com.silencecorner.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author hai
 * @description 用户entity
 * @email hilin2333@gmail.com
 * @date 30/12/2017 5:14 PM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String uid;
    /**
     * 用户名
     */
    @Column(columnDefinition = "VARCHAR(50)")
    private String username;

    /**
     * 密码
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String password;

    /**
     * 盐
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String salt;

    /**
     * 邮箱
     */
    @Column(columnDefinition = "VARCHAR(50)")
    private String email;

    /**
     * 手机号
     */
    @Column(columnDefinition = "VARCHAR(11)",unique = true)
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @Column(columnDefinition = "tinyint(1)")
    private Integer status;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 创建时间
     */
    private Date createDate;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Wallet wallet;

}