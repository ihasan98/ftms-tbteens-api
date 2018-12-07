package com.ftms.ftmsapi.repository;

import com.ftms.ftmsapi.model.PartRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRequestRepository extends JpaRepository<PartRequest, Long> {
}
