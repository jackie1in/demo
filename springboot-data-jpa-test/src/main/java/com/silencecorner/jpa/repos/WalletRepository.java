package com.silencecorner.jpa.repos;

import com.silencecorner.jpa.model.User;
import com.silencecorner.jpa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @description 钱包dao
 * @author hai
 * @email hilin2333@gmail.com
 * @date 16/01/2018 2:55 PM
 */
@Repository
public interface WalletRepository<T,String extends Serializable> extends JpaRepository<Wallet,String> {
}
