package cc.insistor.dao;

import cc.insistor.model.po.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author cc
 */
public interface CommentRepository extends MongoRepository<Comment, String> {

}
