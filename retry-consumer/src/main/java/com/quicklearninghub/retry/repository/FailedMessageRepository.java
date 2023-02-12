package com.quicklearninghub.retry.repository;

import com.quicklearninghub.retry.entity.FailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessageRepository extends JpaRepository<FailedMessage, Long> {
}

