package com.stringee.domain.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Service
public class MongoDbAccess {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MongoClient mongo = null;

    @Value("${app.mongdb.host}")
    private String host;
    @Value("${app.mongdb.port}")
    private Integer port;
    @Value("${app.mongdb.username}")
    private String authUser;
    @Value("${app.mongdb.password}")
    private String authPwd;
    @Value("${app.mongdb.db}")
    private String dbName;

    @Value("${app.mongdb.connectionsPerHost}")
    private Integer connectionsPerHost;
    @Value("${app.mongdb.maxConnectionIdleTime}")
    private Integer maxConnectionIdleTime;
    @Value("${app.mongdb.maxConnectionLifeTime}")
    private Integer maxConnectionLifeTime;

    public MongoClient getMongo() throws RuntimeException {

        if (mongo == null) {
            String encodedPwd = "";
            logger.info("Mongo DB server: {}:{}", host, port);
            logger.info("Mongo DB user: {}", authUser);
            logger.info("Mongo DB pwd: {}", authPwd);
            logger.info("Mongo DB db: {}", dbName);
            logger.info("Mongo DB connectionsPerHost: {}", connectionsPerHost);
            logger.info("Mongo DB maxConnectionIdleTime: {}", maxConnectionIdleTime);
            logger.info("Mongo DB maxConnectionLifeTime: {}", maxConnectionLifeTime);

            MongoClientOptions.Builder options = MongoClientOptions.builder()
                    .connectionsPerHost(connectionsPerHost)
                    .writeConcern(WriteConcern.ACKNOWLEDGED)
                    .maxConnectionIdleTime(maxConnectionIdleTime * 1_000)
                    .maxConnectionLifeTime(maxConnectionLifeTime * 1_000);
            try {
                encodedPwd = URLEncoder.encode(authPwd, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                logger.error("Exception ", ex);
            }

            String clientUrl = "mongodb://" + authUser + ":" + encodedPwd + "@" + host + ":" + port + "/" + dbName;
            MongoClientURI uri = new MongoClientURI(clientUrl, options);

            logger.info("Connect to MongoDB information: {}", uri.toString());
            try {
                mongo = new MongoClient(uri);
            } catch (Exception ex) {
                logger.error("An Exception occoured when connecting to MongoDB", ex);
            }

        }

        return mongo;

    }

}
