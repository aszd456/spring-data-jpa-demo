package com.cooperative.service.user;

import com.cooperative.dao.user.UserDao;
import com.cooperative.entity.user.Department;
import com.cooperative.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    EntityManager em;


    public User getUserById(Integer id) {
        return userDao.getOne(id);
    }

    /**
     * JPA EntityManager的四个主要方法 ——persist,merge,refresh和remove
     * persist 方法可以将实例转换为 managed( 托管 ) 状态。在调用 flush() 方法或提交事物后，实例将会被插入到数据库中。
     * <p>
     * merge 方法的主要作用是将用户对一个 detached（独立） 状态实体的修改进行归档，归档后将产生一个新的 managed 状态对象。
     * <p>
     * refresh 方法可以保证当前的实例与数据库中的实例的内容一致。
     * <p>
     * remove 方法可以将实体转换为 removed 状态，并且在调用 flush() 方法或提交事物后删除数据库中的数据。
     */

    public int updateUser(String name, Integer id) {
        return userDao.updateName(name,id);
    }

    public User findUser(int id) {
        Optional<User> user = userDao.findById(id);
        return user.orElse(null);
    }

    public Integer addUser(User user) {
        userDao.save(user);
        return user.getId();
    }

    public List<User> getAllUser(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<User> pageObject = userDao.findAll(pageable);
        int totalPage = pageObject.getTotalPages();
        int realSize = pageObject.getSize();
        long count = pageObject.getTotalElements();

        return pageObject.getContent();
    }

    public User getUser(String name) {

        return userDao.findByName(name);
    }

    public User getUser(String name, Integer departmentId) {
//		getUser(departmentId);
        return userDao.nativeQuery(name, departmentId);
//		return userDao.findUserByDepartment(name, departmentId);
    }

    private void getUser(Integer departmentId) {
        List<Object[]> list = userDao.queryUserCount();
        List<Integer> ids = userDao.queryUserIds(departmentId);
        int a = 1;
    }

    public Page<User> queryUser(Integer departmentId, Pageable page) {
        return userDao.queryUsers(departmentId, page);
    }

    public Page<User> queryUser2(Integer departmentId, Pageable page) {
        //构造JPQL和实际的参数
        StringBuilder baseSql = new StringBuilder("from user u where 1=1 ");
        Map<String, Object> paras = new HashMap<String, Object>();
        if (departmentId != null) {
            baseSql.append("and  u.department_id=:deptId");
            paras.put("deptId", departmentId);
        }
        //查询满足条件的总数
        long count = getQueryCount(baseSql, paras);
        if (count == 0) {
            return new PageImpl(Collections.emptyList(), page, 0);
        }
        //查询满足条件结果集
        List list = getQueryResult(baseSql, paras, page);


        //返回结果
        Page ret = new PageImpl(list, page, count);
        return ret;
    }

    public List<User> getByExample(String name) {
        User user = new User();
        Department dept = new Department();
        user.setName(name);
        dept.setId(1);
        user.setDepartment(dept);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase());
        Example<User> example = Example.of(user, matcher);
//		Example<User> example = Example.of(user);
        List<User> list = userDao.findAll(example);
        return list;
    }

    private List getQueryResult(StringBuilder baseSql, Map<String, Object> paras, Pageable page) {
        Query query = em.createNativeQuery("select * " + baseSql.toString());
        setQueryParameter(query, paras);
        query.setFirstResult((int) page.getOffset());
        query.setMaxResults(page.getPageNumber());
        List list = query.getResultList();
        return list;
    }

    private Long getQueryCount(StringBuilder baseSql, Map<String, Object> paras) {
        Query query = em.createNativeQuery("select count(1) " + baseSql.toString());
        setQueryParameter(query, paras);
        Number number = (Number) query.getSingleResult();
        return number.longValue();
    }

    private void setQueryParameter(Query query, Map<String, Object> paras) {
        for (Map.Entry<String, Object> entry : paras.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

}
