package com.mydirectory.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class GenericRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public void save(String jsonString){

        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("Clients");

        table.putItem(Item.fromJSON(jsonString));

    }

    public List<String> getAll(){

        String jsonString = "";
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Clients");

        ScanResult result = amazonDynamoDB.scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()){
            jsonString = gson.toJson(item);
            list.add(jsonString);
            System.out.println(jsonString);
        }

        return list;
    }

    public String getById(String id){
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("Clients");

        QuerySpec spec = new QuerySpec()
                .withSelect(Select.ALL_ATTRIBUTES)
                .withKeyConditionExpression("id = :id")
                .withValueMap(new ValueMap()
                        .withString(":id", id));

        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        String jsonString = iterator.next().toJSONPretty();
        return jsonString;
    }
}




