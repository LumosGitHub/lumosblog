package lumosblog.model.dao;

import lumosblog.model.entity.Archive;
import lumosblog.model.entity.Contents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 冠麟
 * @date 2019/10/7 17:50
 */
public interface ContentsDao extends JpaRepository<Contents, Integer> {


    /**
     * 根据 type 字段 查找所有
     * @param type 类型
     * @return {@link List<Contents>}
     */
    public List<Contents> findAllByType(@Param("type") String type);



    /**
     * 根据标签与类型查询所有行数
     * @param tags 标签
     * @param type 类型
     * @return {@link List<Contents>}
     */
    public List<Contents> findAllByTagsLikeAndType(@Param("tags") String tags,String type);


    /**
     * 根据cid 获取单条文章
     *
     * @param cid 文章id
     * @return {@link Contents}
     */
    public Contents findByCid(Integer cid);

    /**
     * 根据 status 查询所有
     *
     * @param status   状态有两种  公开1·publish  草稿2·draft
     * @param pageable 分页参数
     * @param type     行数据类型
     * @return {@link Page<Contents>}
     */
    public Page<Contents> findAllByStatusAndType(@Param("status") String status, String type, Pageable pageable);


    /**
     * 根据参数查询
     *
     * @param title      文章标题 模糊查询
     * @param categories 分类 模糊查询
     * @param status     状态
     * @param type       类型
     * @param pageable   分页参数
     * @return a {@link Page<Contents>}
     */
    public Page<Contents> findAllByTitleLikeAndCategoriesLikeAndStatusLikeAndType(@Param("title") String title, @Param("categories") String categories, @Param("status") String status, @Param("type") String type, Pageable pageable);


    /**
     * 根据类型查询
     *
     * @param type 类型
     * @param pageable   分页参数
     * @return a {@link Page<Contents>}
     */
    public Page<Contents> findAllByType(@Param("type") String type,Pageable pageable);


    /**
     * 查询所有文章
     *
     * @param status
     * @return
     */
    public List<Contents> findAllByStatus(@Param("status") String status);

    /**
     * 查询 后 10条记录
     *
     * @param status 记录状态
     * @return 10条记录
     */
    List<Contents> findFirst10ByStatusOrderByCidDesc(String status);


    /**
     * 查询行数的创建时间
     *
     * @return object[] 有两个元素，0 是 String 类型的 时间 ， 1 是 String 类型的 计数
     */
    @Query(value = "select   distinct  FROM_UNIXTIME(created, '%Y年%m月') as dateFiling, count(*) as count from t_contents where type = 'post' and status = 'publish' group by dateFiling order by dateFiling desc", nativeQuery = true)
    public List<Object[]> getArchivesCount();

    /**
     * 查询 时间戳范围内的行数
     *
     * @param type       行数类型 post
     * @param status     行数状态，是否审核
     * @param created    时间戳起点
     * @param createdMax 时间戳终点
     * @return List<Contents>类型 {@link Contents}
     */
    @Query(value = "select c from Contents c where c.type=:type and c.status=:status and c.created > :created and c.created < :createdMax order by c.created DESC ")
    public List<Contents> getArchives(@Param("type") String type, @Param("status") String status, @Param("created") Integer created, @Param("createdMax") Integer createdMax);


    /**
     * 模糊查询 Title
     * %%可以模糊查询
     *
     * @param title  文章标题
     * @param status 状态
     * @param type   行数据类型
     * @return List<Contents>类型 {@link Contents}
     */
    public List<Contents> findAllByStatusAndTypeAndTitleLike(String status, String type, String title);

    /**
     * 查询 后 5条记录
     *
     * @param status 行 状态
     * @return List<Contents>类型 {@link Contents}
     */
    public List<Contents> findFirst5ByStatusOrderByCidDesc(String status);


    /**
     * 根据 标题 与 状态 查询所有的 行数
     *
     * @param title  标题
     * @param status 状态
     * @return a List<Contents> {@link Contents}
     */
    public List<Contents> findAllByTitleAndStatus(String title, String status);


    /**
     * 根据 标签 与 状态 查询所有的行数
     * %%可以模糊查询
     *
     * @param tags   标签
     * @param status 状态
     * @return a List<Contents> {@link Contents}
     */
    public List<Contents> findAllByStatusAndTypeAndTagsLike(String status, String type, String tags);

    /**
     * 根据 分类 与状态 查询所有的行数
     * %%可以模糊查询
     *
     * @param categories 分类
     * @param status     状态
     * @return a List<Contents> {@link Contents}
     */
    public List<Contents> findAllByStatusAndTypeAndCategoriesLike(String status, String type, String categories);


    /**
     * 查询自定义的地址
     *
     * @param status 状态
     * @param slug   地址
     * @param type   行数据类型
     * @return {@link Contents}
     */
    public Contents findByStatusAndTypeAndSlug(String status, String type, String slug);


}
