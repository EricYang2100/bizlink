package com.welinkdata.gateway.bizTP.repository;

import com.welinkdata.gateway.bizTP.domain.TPInvite;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface TPInviteRepository extends MongoRepository<TPInvite, String> {



}