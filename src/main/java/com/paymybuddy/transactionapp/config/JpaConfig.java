package com.paymybuddy.transactionapp.config;

import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
public class JpaConfig {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRepositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{
                new ClassPathResource("user_account.json")
        });
        return factory;
    }

}