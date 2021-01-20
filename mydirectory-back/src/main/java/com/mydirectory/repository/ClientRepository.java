package com.mydirectory.repository;

import com.mydirectory.entity.Client;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ClientRepository extends CrudRepository<Client, String> {
    //List<Client> findByNom(String nom);

}
