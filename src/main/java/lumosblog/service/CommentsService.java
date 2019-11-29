package lumosblog.service;

import lumosblog.model.dao.CommentsDao;
import lumosblog.model.dto.Comment;
import lumosblog.model.entity.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author 冠麟
 * @date 2019/10/7 17:21
 */
@Service
public class CommentsService {


    @Autowired
    private CommentsDao dto;


    /**
     * 获取所有评论
     *
     * @return
     */
    public List<Comments> getByIdAll() {
        return dto.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public List<Comments> getAllById(Integer id,String status){
        return dto.findAllByCidAndStatus(id,status);
    }
    /**
     * 根据id 获取单条记录
     * @param id 评论id
     * @return a {@link Optional<Comments>}
     */
    public Optional<Comments> getCommentsById(Integer id) {
        return dto.findById(id);
    }

    /**
     * 获取所有评论并分页
     *
     * @param pageable 分页参数
     * @return
     */
    public Page<Comments> getAllPage(Pageable pageable) {
        return dto.findAll(pageable);
    }

    /**
     * 保存或更新对象
     *
     * @param comments 对象
     * @return 已保存的对象
     */
    public Comments save(Comments comments) {
        return dto.save(comments);
    }

    /**
     * 保存评论
     *
     * @param comments 评论bean
     * @return 已保存的对象
     */
    public Comments saveArticle(Comments comments) {

        //子评论
        if (comments.getCoid() != null) {
            comments.setAuthorId(0);
            comments.setOwnerId(1);
            comments.setParent(comments.getCoid());
            comments.setCreated((int) (System.currentTimeMillis() / 1000));
            comments.setCoid(null);
            comments.setStatus("no_audit");
            return dto.save(comments);

        } else {
            //根评论
            comments.setAuthorId(0);
            comments.setOwnerId(1);
            comments.setParent(0);
            comments.setStatus("no_audit");
            comments.setCreated((int) (System.currentTimeMillis() / 1000));
            return dto.save(comments);

        }

    }


    /**
     * 根据文章id 返回相应的评论 并分页处理
     *
     * @param cId      文章id
     * @param status   评论状态
     * @param pageable 分页参数
     * @return Page<Comments> {@link Comments}
     */
    public Page<Comments> getCommentsParent(int cId, String status, Pageable pageable) {
        return dto.selectAllByCidAndParent(cId, 0, status, pageable);
    }


    /**
     * 获取子评论
     *
     * @param cId        文章id
     * @param coIdParent 父级评论id
     * @return
     */
    public List<Comments> getCommentsChild(int cId, int coIdParent, String status) {
        return dto.selectAllByCidAndCoIdAndParent(cId, coIdParent, status);
    }

    /**
     * 获取 10条 最新评论
     *
     * @param status 评论状态
     * @return 10条 最新评论
     */
    public List<Comments> getNewestComment10(String status,String type) {
        return dto.findFirst10ByStatusAndTypeOrderByCoidDesc(status,type);
    }

    /**
     * 获取 5条 最新评论
     *
     * @return List<Comments>
     */
    public List<Comments> getNewestComment5(String type) {
        return dto.findFirst5ByTypeOrderByCoidDesc(type);
    }


    /**
     * 删除评论
     *
     * @param id 评论id
     */
    public void deleteComment(Integer id) {
        dto.deleteById(id);
    }


    /**
     * 修改评论状态
     *
     * @param id     评论id
     * @param status 状态
     * @return integer
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateComment(Integer id, String status) {
        return dto.updateStatusById(status, id);
    }


}
