package com.simon.simonssecureapi.repository;

import com.simon.simonssecureapi.entity.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<AppUser, Long> {
    @EntityGraph(attributePaths = {"member", "member.address"})
    List<AppUser> findAllByMemberIsNotNull();


    @EntityGraph(attributePaths = {"member", "member.address"})
    Optional<AppUser> findByUsername(String username);


}
