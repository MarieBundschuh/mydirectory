package com.mydirectory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.gson.Gson;
import com.mydirectory.entity.Generic;
import com.mydirectory.repository.GenericRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MydirectoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" })
public class ClientsIntegrationTest {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    GenericRepository genericRepository;

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
        List<String> liste =  genericRepository.getAll();
        System.out.println(liste);
        for (String client : liste) {
            System.out.println(client.toString());
        }
    }

    @Test
    public void createGeneric(){
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("Clients");

        Item item = new Item().withPrimaryKey("id", "0")
                .withString("nom", "Tata")
                .withString("prenom", "Marie");
        table.putItem(item);

    }

    @Test
    public void getById(){
        String retour = genericRepository.getById("2021.01.14.19.43.12-0001");
        System.out.println(retour);

    }

    @Test
    public void scanTable(){

        String jsonString = "";
        Gson gson = new Gson();

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Clients");

        ScanResult result = amazonDynamoDB.scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()){
            /*for (Map.Entry mapEntry : item.entrySet()) {
                System.out.println("clé: " + mapEntry.getKey() + " | valeur: " + mapEntry.getValue());
            }*/
            jsonString = gson.toJson(item);
            System.out.println(jsonString);
        }
    }

    @Test
    public void createFromMap(){

        Map<String, String> map = new HashMap<>();
        map.put("prenom", "Marieaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        map.put("age", "31");
        map.put("ville", "lingo");
        map.put("cp", "67380");

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        map.put("id", timeStamp);

        String jsonString = "";
        Gson gson = new Gson();
        jsonString = gson.toJson(map);
        System.out.println(jsonString);

        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("Clients");

        table.putItem(Item.fromJSON(jsonString));

    }
}
