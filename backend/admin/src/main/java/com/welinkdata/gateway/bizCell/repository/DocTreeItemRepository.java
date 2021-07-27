package com.welinkdata.gateway.bizCell.repository;

import com.welinkdata.gateway.bizCell.domain.DocTreeItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocTreeItemRepository extends MongoRepository<DocTreeItem, String> {

    DocTreeItem findFirstByDocPath(String docPath);

    List<DocTreeItem> findAllByParentId(String parentId);
}
