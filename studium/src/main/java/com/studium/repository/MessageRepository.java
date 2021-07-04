package com.studium.repository;

import com.studium.models.Message;
import com.studium.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query(value="SELECT COUNT(*) FROM message  WHERE receiver=:id AND is_read=:myBoolean \n", nativeQuery=true)
    int countByReceiverAndIsRead(Long id, boolean myBoolean);

    List<Message> findAllByReceiver(User user);
}