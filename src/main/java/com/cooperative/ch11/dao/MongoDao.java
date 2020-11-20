package com.cooperative.ch11.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cooperative.ch11.PageModel;
import com.cooperative.ch11.entity.MongoSequence;
import com.google.common.collect.ImmutableMap;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * db.collection.findOne()
     *
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

    public T getOne(Map<String, Object> filter) {
        Assert.notNull(filter, "过滤条件不能为空");
        Query queryFilter = getQueryFilter(filter);
        return mongoTemplate.findOne(queryFilter, this.getEntityClass());
    }

    /**
     * 获取单个model
     *
     * @param filter 过滤条件
     * @param sort   Sort theSort = new Sort(Sort.Direction.ASC, "_id");
     */
    public T getOne(Map<String, Object> filter, Sort sort) {
        Query queryFilter = getQueryFilter(filter);
        queryFilter = queryFilter.with(sort);

        return mongoTemplate.findOne(queryFilter, this.getEntityClass());
    }

    /**
     * 按访问时间倒序排列
     *
     * @param filter 过滤条件
     * @param sort   排序条件
     * @param limit  前几个(0:不限制)
     */
    public List<T> list(Map<String, Object> filter, Sort sort, int limit) {
        Query queryFilter = getQueryFilter(filter);
        if (sort != null) {
            queryFilter = queryFilter.with(sort);
        }
        if (limit > 0) {
            queryFilter = queryFilter.limit(limit);
        }

        return mongoTemplate.find(queryFilter, this.getEntityClass());
    }

    /**
     * 统计
     *
     * @param filter 过滤条件
     */
    public long count(Map<String, Object> filter) {
        Query queryFilter = getQueryFilter(filter);
        return mongoTemplate.count(queryFilter, this.getEntityClass());
    }

    private Query getQueryFilter(ObjectId id) {
        return getQueryFilter(ImmutableMap.of("_id", id));
    }

    private long count(Query queryFilter) {
        return mongoTemplate.count(queryFilter, this.getEntityClass());
    }

    /**
     * 分页查询
     *
     * @param filter    过滤条件
     * @param sort      排序条件
     * @param pageIndex 第几页
     * @param pageSize  每页大小
     */
    public PageModel<T> pagedList(Map<String, Object> filter, Sort sort, int pageIndex, int pageSize) {
        Query queryFilter = getQueryFilter(filter);
        long count = count(queryFilter);
        if (sort != null) {
            queryFilter = queryFilter.with(sort);
        }
        queryFilter = queryFilter.skip((pageIndex - 1) * pageSize).limit(pageSize);
        List<T> subItems = mongoTemplate.find(queryFilter, this.getEntityClass());
        int pages = (int) Math.ceil((double) count / (double) pageSize);
        return new PageModel<T>(pageIndex, pageSize, count, pages, subItems);
    }

    public long updateOne(String id, Map<String, Object> filedValues) {
        return updateOne(new ObjectId(id), filedValues);
    }

    public long updateOne(ObjectId id, Map<String, Object> filedValues) {
        Query queryFilter = getQueryFilter(id);
        return updateImpl(queryFilter, filedValues);
    }

    public long update(Map<String, Object> filter, Map<String, Object> filedValues) {
        Query queryFilter = getQueryFilter(filter);

        return updateImpl(queryFilter, filedValues);
    }

    public boolean exists(Map<String, Object> filter) {
        return getOne(filter) != null;
    }

    /**
     * 删除
     *
     * @param id _id
     */
    public long delete(String id) {
        Query queryFilter = getQueryFilter(new ObjectId(id));

        return delete(queryFilter);
    }

    /**
     * 删除
     *
     * @param id _id
     */
    public long delete(ObjectId id) {
        Query queryFilter = getQueryFilter(id);

        return delete(queryFilter);
    }

    /**
     * 删除
     *
     * @param filter 用户Id
     */
    public long delete(Map<String, Object> filter) {
        Query queryFilter = getQueryFilter(filter);

        return delete(queryFilter);
    }

    /**
     * 软删除
     *
     * @param filter 用户Id
     */
    public long softDelete(Map<String, Object> filter) {
        Query queryFilter = getQueryFilter(filter);
        UpdateResult result = mongoTemplate.updateMulti(queryFilter, Update.update("isDel", 1), this.getEntityClass());

        return result.getModifiedCount();
    }

    /**
     * 根据当前表名获取自增id
     * <p>
     * collectionName 表名
     *
     * @return 自增id
     */
    public int getNextSequence() {
        String collectionName = mongoTemplate.getCollectionName(this.getEntityClass());
        Query query = new Query(Criteria.where("_id").is(collectionName));
        Update update = new Update();
        update.inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);

        MongoSequence item = mongoTemplate.findAndModify(query, update, options, MongoSequence.class);

        return item.getSeq();
    }

    /**
     * 删除
     *
     * @param queryFilter 用户Id
     */
    private long delete(Query queryFilter) {
        return mongoTemplate.remove(queryFilter, this.getEntityClass()).getDeletedCount();
    }

    private long updateImpl(Query queryFilter, Map<String, Object> filedValues) {
        Update updateOperations = new Update();
        for (Map.Entry<String, Object> entry : filedValues.entrySet()) {
            updateOperations.set(entry.getKey(), entry.getValue());
        }
        UpdateResult writeResult = mongoTemplate.updateMulti(queryFilter, updateOperations, this.getEntityClass());
        return writeResult.getModifiedCount();
    }

    private Query getQueryFilter(Map<String, Object> filter) {
        Query query = new Query();
        if (CollectionUtils.isEmpty(filter)) {
            return query;
        }
        Criteria criteria = new Criteria();
        boolean w = false;
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            if (!w) {
                criteria = Criteria.where(entry.getKey()).is(entry.getValue());
                w = true;
            } else {
                criteria = criteria.and(entry.getKey()).is(entry.getValue());
            }
            query.addCriteria(criteria);
        }
        return query;
    }

    /**
     * 功能描述: 创建一个集合
     * 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能，
     * 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
     *
     * @param name 集合名称，相当于传统数据库的表名
     * @return:void
     */
    public void createCollection(String name) {
        mongoTemplate.createCollection(name);
    }

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     * @return:java.lang.String
     */
    public String createIndex(String collectionName, String filedName) {
        //配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一
        options.unique(true);
        //创建按filedName升序排的索引
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(filedName), options);
    }

    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName
     * @return:java.util.List<java.lang.String>
     */
    public List<String> getAllIndexes(String collectionName) {
        ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
        //上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
        List<String> indexes = new ArrayList<>();
        for (org.bson.Document document : list) {
            document.entrySet().forEach((key) -> {
                //提取出索引的名称
                if (key.getKey().equals("name")) {
                    indexes.add(key.getValue().toString());
                }
            });
        }
        return indexes;
    }

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     *
     * @param infos 对象列表
     * @return:void
     */

    public void insertMulti(List<T> infos) {
        mongoTemplate.insert(infos, this.getEntityClass());
    }

    /**
     * 功能描述: 使用索引信息精确更改某条数据
     *
     * @param id             唯一键
     * @param collectionName 集合名称
     * @param info           待更新的内容
     * @return:void
     */
    public void updateById(String id, String collectionName, T info) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        String str = JSON.toJSONString(info);
        JSONObject jQuery = JSON.parseObject(str);
        jQuery.forEach((key, value) -> {
            //因为id相当于传统数据库中的主键，这里使用时就不支持更新，所以需要剔除掉
            if (!key.equals("id")) {
                update.set(key, value);
            }
        });
        mongoTemplate.updateMulti(query, update, info.getClass(), collectionName);
    }

    /**
     * 功能描述: 根据id删除集合中的内容
     *
     * @param id             序列id
     * @param collectionName 集合名称
     * @param clazz          集合中对象的类型
     * @return:void
     */

    public void deleteById(String id, Class<T> clazz, String collectionName) {
        // 设置查询条件，当id=#{id}
        Query query = new Query(Criteria.where("id").is(id));
        // mongodb在删除对象的时候会判断对象类型，如果你不传入对象类型，只传入了集合名称，它是找不到的
        // 上面我们为了方便管理和提升后续处理的性能，将一个集合限制了一个对象类型，所以需要自行管理一下对象类型
        // 在接口传入时需要同时传入对象类型
        mongoTemplate.remove(query, clazz, collectionName);
    }

    /**
     * 功能描述: 根据id查询信息
     *
     * @param id             注解
     * @param clazz          类型
     * @param collectionName 集合名称
     * @return:java.util.List<T>
     */

    public T selectById(String id, Class<T> clazz, String collectionName) {
        // 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
        return mongoTemplate.findById(id, clazz, collectionName);
    }

    /**
     * 功能描述: 分页查询列表信息
     *
     * @param collectName 集合名称
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @return:java.util.List<T>
     */
    public List<T> selectList(String collectName, Class<T> clazz, Integer currentPage, Integer pageSize) {
        //设置分页参数
        Query query = new Query();
        //设置分页信息
        if (!ObjectUtils.isEmpty(currentPage) && ObjectUtils.isEmpty(pageSize)) {
            query.limit(pageSize);
            query.skip(pageSize * (currentPage - 1));
        }
        return mongoTemplate.find(query, clazz, collectName);
    }

    /**
     * 功能描述: 根据条件查询集合
     *
     * @param collectName 集合名称
     * @param conditions  查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @return:java.util.List<T>
     */

    public List<T> selectByCondition(String collectName, Map<String, String> conditions, Class<T> clazz, Integer currentPage, Integer pageSize) {
        if (ObjectUtils.isEmpty(conditions)) {
            return selectList(collectName, clazz, currentPage, pageSize);
        } else {
            //设置分页参数
            Query query = new Query();
            query.limit(pageSize);
            query.skip(currentPage);
            // 往query中注入查询条件
            conditions.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));
            return mongoTemplate.find(query, clazz, collectName);
        }
    }

    /**
     * 功能描述: 根据条件查询集合
     *
     * @param collectName 集合名称
     * @param query       查询条件
     * @param clazz       对象类型
     * @return
     */
    public List<T> selectByQuery(String collectName, Query query, Class<T> clazz) {
        List<T> result = mongoTemplate.find(query, clazz, collectName);
        return result;
    }


}
