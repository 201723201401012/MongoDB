package cc.insistor;

import cc.insistor.dao.CommentRepository;
import cc.insistor.model.po.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void contextLoads() {


    }

    @Test
    void queryAndUpdate() {
        Query query = new Query(Criteria.where("id").is("5fc44e4dcc88e434d4ad4adb"));
        Query id = Query.query(Criteria.where("id").is("5fc44e4dcc88e434d4ad4adb"));

        Update update = new Update();
        update.set("nickname", "cc1110");
        mongoTemplate.updateFirst(id, update, Comment.class);
    }

    @Test
    void findA() {
        List<Comment> all = mongoTemplate.findAll(Comment.class);
        for (Comment it : all) {
            System.out.println(it);
        }
    }

    @Test
    void deleteById(){
        Comment byId = mongoTemplate.findById("5fc44e4dcc88e434d4ad4adb", Comment.class);
        mongoTemplate.remove(byId);

    }
    @Test
    void queryByA(){
        String collectionName = "nickname";
        PageRequest of = PageRequest.of(0, 10);

        Query query = Query.query(Criteria.where("state").is("1")).with(of);

        List<Comment> state = mongoTemplate.find(query, Comment.class,"comment");

        System.out.println("size="+state.size());
        for(Comment it:state){
            System.out.println(it);
        }

    }

    /**
     * SQL: name not like '张%'
     *      Criteria.where("name").regex(Pattern.compile("^张.*$"));
     *
     * SQL: interest like '%球%'
     *      Criteria.where("interest").regex(Pattern.compile("^.*球.*$"));
     *
     * SQL: address not like '浙江%'
     *      Criteria.where("address").not().regex(Pattern.compile("^.*浙江.*$"));
     *
     * SQL: interest not null and interest != '' and interest = '无'
     *      Criteria.where("interest").is(null).is("").is("无");
     *
     * SQL: interest = '户外' or interest = '阅读' or interest = '音乐'
     *      new Criteria().orOperator(
     *          Criteria.where("interest").is("户外"),
     *          Criteria.where("interest").is("阅读"),
     *          Criteria.where("interest").is("音乐"));
     *
     * SQL: (address like '%杭州%' and sex = 1) or (address like '%苏州%' and sex = 0)
     *      new Criteria().orOperator(
     *          Criteria.where("address").regex(Pattern.compile("^.*杭州.*$")).and("sex").is(1),
     *          Criteria.where("address").regex(Pattern.compile("^.*苏州.*$")).and("sex").is(0));
     *
     * SQL: (interest = '户外' and sex = 0) or (interest = '音乐' and sex = 1)
     *      new Criteria().orOperator(
     *          new Criteria().andOperator(Criteria.where("interest").is("户外"), Criteria.where("sex").is(0)),
     *          new Criteria().andOperator(Criteria.where("interest").is("音乐"), Criteria.where("sex").is(1)));
     */

    @Test
    void test1(){
        Query query = Query.query(Criteria.where("nickname").regex("^相.*$"));
        Query query1 = Query.query(new Criteria().orOperator(Criteria.where("id").is("1"), Criteria.where("nickname").regex("^杰.*$")));
        Query query2 = Query.query(Criteria.where("id").in(Arrays.asList("1", "2", "3")));
        Query query3 = Query.query(Criteria.where("id").gt("2"));
        Query query4 = Query.query(Criteria.where("id").is("2").and("articleid").is("100001"));



        List<Comment> comment = mongoTemplate.find(query4, Comment.class, "comment");
        System.out.println("size="+comment.size());
        for(Comment it:comment){
            System.out.println(it);
        }

    }

    @Test
    void test2(){
        commentRepository.deleteById("ff");
        List<Comment> all = commentRepository.findAll();
        Optional<Comment> byId = commentRepository.findById("1");
        Comment comment = commentRepository.findById("1").get();
    }

}
