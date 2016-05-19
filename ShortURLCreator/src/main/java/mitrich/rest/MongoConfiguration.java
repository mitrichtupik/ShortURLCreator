package mitrich.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories
public class MongoConfiguration extends AbstractMongoConfiguration {

	private String databaseName = "restdb";
	private String host = "127.0.0.1";
	private int port = 27017;
	private String mappingBasePackage = "mitrich.rest.model";

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(host, port);
	}

	@Override
	protected String getMappingBasePackage() {
		return mappingBasePackage;
	}

}
