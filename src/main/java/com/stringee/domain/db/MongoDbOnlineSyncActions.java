package com.stringee.domain.db;

import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Component
public class MongoDbOnlineSyncActions extends BaseMongoAction {

    @Value("${app.mongdb.db}")
    private String dbName;

    private final MongoDbAccess dbAccess;

    public MongoDbOnlineSyncActions(MongoDbAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public boolean checkCardExisted(String pin, String serial) {
        long c = 0;
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Bson cond = Filters.and(
                    Filters.eq("pin", pin),
                    Filters.eq("serial", serial)
            );
            c = database.getCollection("cards").countDocuments(cond);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return c == 0;
    }

    public long delete(String collection, Bson condition) {
        long ret = 0L;
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (condition == null) {
                condition = new Document();
            }
            DeleteResult result = database.getCollection(collection).deleteMany(condition);
            ret = result.getDeletedCount();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return ret;
    }

    public List<Document> findAll(String collection, Bson params, Bson sort, int start, int limit) {
        List<Document> result = new ArrayList<>();
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (params == null) {
                params = new Document();
            }
            if (sort == null) {
                sort = new Document();
            }
            FindIterable<Document> iterable = database.getCollection(collection).find(params).sort(sort).skip(start).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                document.remove("_id");
                result.add(document);
            }
            return result;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return result;
    }

    public FindIterable<Document> findAll2(String collection, Bson params, Bson sort, int start, int limit) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (params == null) {
                params = new Document();
            }
            if (sort == null) {
                sort = new Document();
            }
            return database.getCollection(collection).find(params).sort(sort).skip(start).limit(limit);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public FindIterable<Document> find(String collection, Bson params, Bson projects) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (params == null) {
                params = new Document();
            }
            return database.getCollection(collection).find(params).projection(projects);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public long countAll(String collection, Bson params) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (params == null) {
                params = new Document();
            }
            return database.getCollection(collection).countDocuments(params);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return 0;
    }

    public Document findOne(String collectionName, Map<String, Object> params) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Document p = new Document();
            if (!params.isEmpty()) {
                p.putAll(params);
            }
            return database.getCollection(collectionName).find(p).first();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public Document findOne(String collectionName, Bson params) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database.getCollection(collectionName).find(params).first();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public List<Document> find(String collectionName, Map<String, Object> params) {
        List<Document> result = null;
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Document p = new Document();
            if (!params.isEmpty()) {
                p.putAll(params);
            }
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return result;
    }

    public List<Document> find(String collectionName, Map<String, Object> params, Bson sort, int start, int limit) {
        List<Document> result = null;
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Document p = new Document();
            if (!params.isEmpty()) {
                p.putAll(params);
            }
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p).sort(sort).skip(start).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return result;
    }

    public List<Document> find(String collectionName, Map<String, Object> params, int limit) {
        List<Document> result = null;
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Document p = new Document();
            if (!params.isEmpty()) {
                p.putAll(params);
            }
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return result;
    }

    public long update(String collectionName, Bson condition, Bson values) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            UpdateResult result = database.getCollection(collectionName).updateMany(condition, values);
            logger.info("update: {} result: {}", collectionName, result);
            return result.getModifiedCount();
        } catch (Exception ex) {
            logger.error("Ex: ", ex);
        }
        return 0;
    }

    public long update(String collectionName, Bson condition, Bson values, boolean upsert) throws Exception {
        MongoClient mongoClient = dbAccess.getMongo();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        UpdateOptions options = new UpdateOptions().upsert(upsert);
        UpdateResult result = database.getCollection(collectionName).updateMany(condition, values, options);
        logger.info("update: {} result: {}", collectionName, result);
        return result.getModifiedCount();
    }

    public void bulkUpdate(String collectionName, List<WriteModel<Document>> data) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            BulkWriteOptions bulkWriteOptions = new BulkWriteOptions();
            bulkWriteOptions.ordered(false);
            BulkWriteResult result = database.getCollection(collectionName).bulkWrite(data, bulkWriteOptions);
            logger.info("update: {} result: {}", collectionName, result);
        } catch (Exception ex) {
            logger.error("Ex: ", ex);
        }
    }

    public void insertOne(String collectionName, Document document) throws Exception {
        MongoClient mongoClient = dbAccess.getMongo();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.getCollection(collectionName).insertOne(document);
    }

    public void insertMany(String collectionName, List<Document> documents) {
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            database.getCollection(collectionName).insertMany(documents);
        } catch (Exception ex) {
            logger.error("Ex: ", ex);
        }
    }

    public List<Document> findAll3(String collection, Bson params, Bson projection, Bson sort, int start, int limit) {
        List<Document> result = new ArrayList<>();
        try {
            MongoClient mongoClient = dbAccess.getMongo();
            MongoDatabase database = mongoClient.getDatabase(dbName);
            if (params == null) {
                params = new Document();
            }
            if (sort == null) {
                sort = new Document();
            }
            if (projection == null) {
                projection = new Document();
            }
            FindIterable<Document> iterable = database.getCollection(collection)
                    .find(params)
                    .projection(projection)
                    .sort(sort)
                    .skip(start)
                    .limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                document.remove("_id");
                result.add(document);
            }
            return result;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return result;
    }

}
