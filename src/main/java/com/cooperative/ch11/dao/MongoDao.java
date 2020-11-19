package com.cooperative.ch11.dao;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @ClassName MongoDbDao
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/19 10:57
 * @Version 1.0
 **/
public abstract class MongoDao<T> {
    protected Logger logger = LoggerFactory.getLogger(MongoDao.class);

    /**
     * 反射获取泛型类型
     *
     * @return
     */
    protected abstract Class<T> getEntityClass();

    @Autowired
    private MongoTemplate mongoTemplate;

    /***
     * 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     * 保存一个对象
     * @param t
     */
    public void save(T t) {
        logger.info("-------------->MongoDB save start");
        this.mongoTemplate.save(t);
    }

    /**
     * 若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
     * db.collection.insert()
     * db.collection.insertMany()
     *
     * @param t
     * @return
     */
    public T insert(T t) {
        return this.mongoTemplate.insert(t);
    }

    /***
     * 根据id从几何中查询对象,db.collection.find(query)
     * @param id
     * @return
     */
    public T queryById(Integer id) {
        Query query = new Query(Criteria.where("_id").is(id));
        logger.info("-------------->MongoDB find start");
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    public T findById(String id) {
        return this.mongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 根据条件查询集合
     *
     * @param object
     * @return
     */
    public List<T> queryList(T object) {
        Query query = getQueryByObject(object);
        logger.info("-------------->MongoDB find start");
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public List<T> queryLike(String param, Pattern pattern) {
        Query query = new Query(Criteria.where(param).regex(pattern));
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public List<T> queryListByCriteria(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 根据条件查询只返回一个文档
     *db.collection.findOne()
     * @param object
     * @return
     */
    public T queryOne(T object) {
        Query query = getQueryByObject(object);
        logger.info("-------------->MongoDB find start");
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    /***
     * 根据条件分页查询
     * @param object
     * @param start 查询起始值
     * @param size  查询大小
     * @return
     */
    public List<T> getPage(T object, int start, int size, Sort sort) {
        Query query = getQueryByObject(object);
//        query.with(Sort.by(Sort.Direction.DESC,"time"));
        if (sort != null) {
            query.with(sort);
        }
        query.skip(start);
        query.limit(size);
        logger.info("-------------->MongoDB queryPage start");
        return this.mongoTemplate.find(query, this.getEntityClass());
    }

    /***
     * 根据条件查询库中符合条件的记录数量
     * @param object
     * @return
     */
    public Long getCount(T object) {
        Query query = getQueryByObject(object);
        logger.info("-------------->MongoDB Count start");
        return this.mongoTemplate.count(query, this.getEntityClass());
    }

    /***
     * 删除对象
     * @param t
     * @return
     */
    public int delete(T t) {
        logger.info("-------------->MongoDB delete start");
        return (int) this.mongoTemplate.remove(t).getDeletedCount();
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void deleteById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
            logger.info("-------------->MongoDB deleteById start");
            if (obj != null) {
                this.delete(obj);
            }
        }
    }
    /*MongoDB中更新操作分为三种
     * 1：updateFirst     修改第一条
     * 2：updateMulti     修改所有匹配的记录
     * 3：upsert  修改时如果不存在则进行添加操作
     * */

    /**
     * 修改匹配到的第一条记录
     *
     * @param srcObj
     * @param targetObj
     */
    public void updateFirst(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        logger.info("-------------->MongoDB updateFirst start");
        this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
    }

    /***
     * 修改匹配到的所有记录
     * @param srcObj
     * @param targetObj
     */
    public UpdateResult updateMultiBy(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        logger.info("-------------->MongoDB updateFirst start");
        return updateMulti(query, update);
    }

    public UpdateResult updateMulti(Query query, Update update) {
        return this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
    }

    /***
     * 修改匹配到的记录，若不存在该记录则进行添加
     * @param srcObj
     * @param targetObj
     */
    public void updateInsert(T srcObj, T targetObj) {
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        logger.info("-------------->MongoDB updateInsert start");
        this.mongoTemplate.upsert(query, update, this.getEntityClass());
    }

    /**
     * 将查询条件对象转换为query
     *
     * @param object
     * @return
     * @author Jason
     */
    private Query getQueryByObject(T object) {
        Query query = new Query();
        String[] fields = getFieldName(object);
        Criteria criteria = new Criteria();
        for (int i = 0; i < fields.length; i++) {
            String filedName = fields[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null) {
                criteria.and(filedName).is(filedValue);
            }
        }
        query.addCriteria(criteria);
        return query;
    }

    /**
     * 将查询条件对象转换为update
     *
     * @param object
     * @return
     * @author Jason
     */
    private Update getUpdateByObject(T object) {
        Update update = new Update();
        String[] fields = getFieldName(object);
        for (int i = 0; i < fields.length; i++) {
            String filedName = fields[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if (filedValue != null) {
                update.set(filedName, filedValue);
            }
        }
        return update;
    }

    /***
     * 获取对象属性返回字符串数组
     * @param o
     * @return
     */
    private static String[] getFieldName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];

        for (int i = 0; i < fields.length; ++i) {
            fieldNames[i] = fields[i].getName();
        }

        return fieldNames;
    }

    /***
     * 根据属性获取对象属性值
     * @param fieldName
     * @param o
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String e = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + e + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[0]);
            return method.invoke(o, new Object[0]);
        } catch (Exception var6) {
            return null;
        }
    }

}
