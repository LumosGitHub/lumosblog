package lumosblog.model.dao;

import lumosblog.model.entity.Comments;
import org.hibernate.dialect.pagination.TopLimitHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author 冠麟
 * @date 2019/10/7 17:15
 */
public interface CommentsDao extends JpaRepository<Comments, Integer> {



    /**
     * 查询父级评论
     * @param cId   文章id
     * @param parent    标识 父级评论
     * @param status    评论状态
     * @param pageable  分页
     * @return
     */
    @Query("select c from Comments c where c.cid=:cId and c.parent=:parent and c.status=:status")
    Page<Comments> selectAllByCidAndParent(@Param("cId") Integer cId, @Param("parent") Integer parent, @Param("status") String status,Pageable pageable);


    /**
     * 查询子评论
     *
     * @param cId        文章id
     * @param coIdParent 父级评论id
     *@param status    评论状态
     * @return
     */
    @Query("select c from Comments c where c.cid=:cId and c.parent=:coIdParent and c.status=:status")
    List<Comments> selectAllByCidAndCoIdAndParent(@Param("cId") Integer cId, @Param("coIdParent") Integer coIdParent,@Param("status") String status);

    /**
     *  查询 后 10条记录
     * @param status 评论状态
     * @return List<Comments>
     */
    List<Comments> findFirst10ByStatusOrderByCoidDesc(String status);


    /**
     *  查询 后 5条记录
     * @return List<Comments>
     */
    List<Comments> findFirst5ByOrderByCoidDesc();

    /**
     * 修改评论状态
     * @param status 状态
     * @param id 评论id
     * @return integer
     */
    @Modifying
    @Query(value = "UPDATE Comments  SET status = :status WHERE coid =:coid")
    Integer updateStatusById(@Param("status") String status,@Param("coid") Integer id);


}
