package mitrich.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import mitrich.rest.repository.UrlRepository;
import mitrich.rest.repository.UserRepository;

@Configuration
@PropertySource(value = "classpath:application.properties")
@EnableMongoRepositories(basePackageClasses = { UrlRepository.class, UserRepository.class })
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Value("${mongo.databaseName}")
	private String databaseName;
	@Value("${mongo.host}")
	private String host;
	@Value("${mongo.port}")
	private int port;
	@Value("${mongo.mappingBasePackage}")
	private String mappingBasePackage;

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
