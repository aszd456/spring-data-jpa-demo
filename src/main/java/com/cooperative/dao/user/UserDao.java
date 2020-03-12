package com.cooperative.dao.user;

import com.cooperative.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByName(String name);

    @Query("select u from User u where u.name=?1 and u.department.id=?2")
    User findUserByDepartment(String name, Integer departmentId);

    @Query(value = "select * from user where name=?1 and department_id=?2", nativeQuery = true)
    User nativeQuery(String name, Integer departmentId);

    @Query(value = "select * from user where name=:name and department_id=:departmentId", nativeQuery = true)
    User nativeQuery2(@Param("name") String name, @Param("departmentId") Integer departmentId);

    @Query(value = "select department_id,count(1) total from user group by department_id", nativeQuery = true)
    List<Object[]> queryUserCount();

    @Query(value = "select id from user where department_id=?1", nativeQuery = true)
    List<Integer> queryUserIds(Integer departmentId);

    @Query(value = "select u from User u where u.department.id=?1")
    Page<User> queryUsers(Integer departmentId, Pageable page);

    @Modifying
    @Query(value = "update user u set u.name=?1 where u.id=?2", nativeQuery = true)
    int updateName(String name, Integer id);
}
