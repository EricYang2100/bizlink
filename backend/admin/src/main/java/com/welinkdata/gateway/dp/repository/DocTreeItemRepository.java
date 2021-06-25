package com.welinkdata.gateway.dp.repository;

import com.welinkdata.gateway.dp.domain.DocTreeItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocTreeItemRepository extends MongoRepository<DocTreeItem, String> {

    DocTreeItem findFirstByDocPath(String docPath);

    List<DocTreeItem> findAllByParentId(String parentId);
}
