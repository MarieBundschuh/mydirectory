package com.mydirectory.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@DynamoDBTable(tableName = "Clients")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String nom;

    @DynamoDBAttribute
    private String prenom;

    @DynamoDBAttribute
    private String adresse;

    @DynamoDBAttribute
    private String codePostal;

    @DynamoDBAttribute
    private  String ville;

    @DynamoDBAttribute
    private String pays;

    @DynamoDBAttribute
    private String telephone;

}
