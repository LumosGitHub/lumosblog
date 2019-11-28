package lumosblog.model.dao;

import lumosblog.model.entity.Metas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 冠麟
 * @date 2019/11/15 18:58
 */
public interface MetasDao extends JpaRepository<Metas, Integer> {


    /**
     * 根据类型获取所有
     *
     * @param type 类型
     * @return a {@link List<Metas>}
     */
    public List<Metas> findAllByType(String type);

    /**
     * 根据 行ID 与行类型 查询单条记录
     *
     * @param id   行id
     * @param type 类型
     * @return {@link Metas}
     */
    public Metas findByMidAndType(@Param("mid") Integer id, @Param("type") String type);


    /**
     * 根据标签名获取
     *
     * @param name 标签名
     * @param type 类型
     * @return {@link Metas}
     */
    public Metas findByNameAndTypeLike(@Param("name") String name, @Param("type") String type);


}
