package com.homelab.authservice.security.authentication.shared.repository;

import com.homelab.authservice.security.authentication.shared.entity.Role;
import com.homelab.authservice.security.authentication.shared.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH r.permissions p
            WHERE u.username = :username
            """)
    Optional<User> findByUsername(@Param("username") String username);

    boolean existsByUsernameAndLockedUntilAfter(
            String username,
            LocalDateTime now
    );

    @Modifying
    @Query("""
                update User u
                set u.failedLoginCount =
                    case
                        when u.lockedUntil is not null
                             and u.lockedUntil > :now
                            then u.failedLoginCount
            
                        when u.lockedUntil is not null
                             and u.lockedUntil <= :now
                            then 1
            
                        else u.failedLoginCount + 1
                    end,
                    u.lockedUntil =
                    case
                        when u.lockedUntil is not null
                             and u.lockedUntil > :now
                            then u.lockedUntil
            
                        when u.lockedUntil is not null
                             and u.lockedUntil <= :now
                            then null
            
                        when u.failedLoginCount + 1 > :maxAttemptCount
                            then :lockedUntil
            
                        else null
                    end,
            
                    u.updatedAt = :now
            
                where u.username = :username
            """)
    int recordLoginFailureCount(
            String username,
            LocalDateTime lockedUntil,
            int maxAttemptCount,
            LocalDateTime now
    );


    @Modifying
    @Query("""
                    update User u
                    set u.failedLoginCount = 0 ,
                        u.lockedUntil  = null ,
                        u.updatedAt = :now
                    where u.username = :username
            """)
    int resetFailureCount(@Param("username") String username,
                          @Param("now") LocalDateTime now);

    User findUserByRoles(Set<Role> roles);
}
