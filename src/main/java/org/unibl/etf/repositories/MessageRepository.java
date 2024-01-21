package org.unibl.etf.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    Page<MessageEntity> findAllByReceiverIdOrSenderIdOrderByCreatedAtDesc(Long receiver_id, Long sender_id, Pageable pageable);
}
