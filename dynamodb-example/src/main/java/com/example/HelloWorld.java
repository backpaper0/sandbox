package com.example;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class HelloWorld {

	public static void main(String[] args) {
		try (DynamoDbClient client = DynamoDbClient.builder()
				.endpointOverride(URI.create("http://localhost:8000"))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("demo", "secret")))
				.region(Region.AP_NORTHEAST_1)
				.build()) {

			ListTablesResponse listTablesResponse = client.listTables();

			if (listTablesResponse.tableNames().contains("Greeting") == false) {
				CreateTableResponse createTableResponse = client.createTable(CreateTableRequest.builder()
						.tableName("Greeting")
						.attributeDefinitions(
								AttributeDefinition.builder().attributeName("Id").attributeType(ScalarAttributeType.S)
										.build())
						.keySchema(KeySchemaElement.builder().attributeName("Id").keyType(KeyType.HASH).build())
						.billingMode(BillingMode.PAY_PER_REQUEST)
						.build());
				System.out.println(createTableResponse);
			}

			ScanResponse scanResponse = client
					.scan(ScanRequest.builder().tableName("Greeting").attributesToGet("Id").build());
			System.out.println(scanResponse);

			for (Map<String, AttributeValue> item : scanResponse.items()) {
				DeleteItemResponse deleteItemResponse = client
						.deleteItem(DeleteItemRequest.builder().tableName("Greeting").key(item).build());
				System.out.println(deleteItemResponse);
			}

			String id = UUID.randomUUID().toString();
			PutItemResponse putItemResponse = client.putItem(PutItemRequest.builder()
					.tableName("Greeting")
					.item(Map.of("Id", AttributeValue.builder().s(id).build(),
							"Message", AttributeValue.builder().s("Hello World").build(),
							"List", AttributeValue.builder().ss(List.of("foo", "bar", "baz")).build(),
							"Map",
							AttributeValue.builder().m(Map.of("foobar", AttributeValue.builder().s("hoge").build()))
									.build()))
					.build());
			System.out.println(putItemResponse);

			GetItemResponse getItemResponse = client
					.getItem(GetItemRequest.builder().tableName("Greeting")
							.key(Map.of("Id", AttributeValue.builder().s(id).build()))
							.build());
			System.out.println(getItemResponse);
		}
	}
}
