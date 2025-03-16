package com.neo.repository;

import com.neo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * @param userName 用户名
     * @return 匹配的用户对象，若无匹配则返回 null
     */
    Optional<User> findByUserName(String userName);

    /**
     * 根据用户名或邮箱查找用户
     * @param username 用户名
     * @param email 邮箱
     * @return 匹配的用户对象，若无匹配则返回 null
     */
    Optional<User> findByUserNameOrEmail(String username, String email);

    /**
     * 修改指定 ID 的用户名称
     * @param userName 新的用户名
     * @param id 用户 ID
     * @return 受影响的行数
     */
    @Transactional(timeout = 30) // 根据实际业务需求调整超时时间
    @Modifying
    @Query("update User set userName = :userName where id = :id")
    int modifyById(@NonNull String userName, @NonNull Long id);

    /**
     * 删除指定 ID 的用户
     * @param id 用户 ID
     */
    @Transactional
    @Modifying
    @Query("delete from User where id = :id")
    void deleteById(@NonNull Long id);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 匹配的用户对象，若无匹配则返回 null
     */
    Optional<User> findByEmail(String email);

    /**
     * 查询所有用户并分页
     * @param pageable 分页信息
     * @return 分页结果
     */
    @Query("select u from User u")
    Page<User> findAll(Pageable pageable);

    /**
     * 根据昵称查询用户并分页
     * @param nickName 昵称
     * @param pageable 分页信息
     * @return 分页结果
     */
    Page<User> findByNickName(@NonNull String nickName, Pageable pageable);

    /**
     * 根据昵称和邮箱查询用户并分页
     * @param nickName 昵称
     * @param email 邮箱
     * @param pageable 分页信息
     * @return 分页结果
     */
    Slice<User> findByNickNameAndEmail(@NonNull String nickName, @NonNull String email, Pageable pageable);
}
