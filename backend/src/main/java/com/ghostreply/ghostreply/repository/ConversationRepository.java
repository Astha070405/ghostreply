package com.ghostreply.ghostreply.repository;

import com.ghostreply.ghostreply.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {

}
