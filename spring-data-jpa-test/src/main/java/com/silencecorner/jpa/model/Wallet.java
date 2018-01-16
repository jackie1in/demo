package com.silencecorner.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author hai 钱包model
 * @description
 * @email hilin2333@gmail.com
 * @date 16/01/2018 2:26 PM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Wallet {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(columnDefinition = "int(11)")
    private Integer balance;

    @OneToOne(mappedBy = "wallet")
    private User user;
}
