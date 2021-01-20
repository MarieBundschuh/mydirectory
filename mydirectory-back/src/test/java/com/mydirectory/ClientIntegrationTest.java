package com.mydirectory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.mydirectory.entity.Client;
import com.mydirectory.entity.Generic;
import com.mydirectory.repository.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MydirectoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" })*/
public class ClientIntegrationTest {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    ClientRepository clientRepository;

    @Before
    public void setup() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(Generic.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));

        try {
            amazonDynamoDB.createTable(tableRequest);
        } catch (ResourceInUseException e) {
            System.out.println("Table " + tableRequest + " already exists.");
        }
    }

    @Test
    public void listAll(){
        Iterable<Client> liste =  clientRepository.findAll();
        System.out.println(liste);
        for (Client client : liste) {
            System.out.println(client.toString());
        }
    }

    @Test
    public void createClient(){
        Client client = new Client();
        client.setNom("Toto");
        client.setPrenom("Tata");
        client.setCodePostal("67000");
        client.setVille("Strasbourg");
        clientRepository.save(client);
    }
}
