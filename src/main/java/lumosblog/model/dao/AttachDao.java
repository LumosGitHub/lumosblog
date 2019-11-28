package lumosblog.model.dao;

import lumosblog.model.entity.Attach;
import lumosblog.model.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 冠麟
 * @date 2019/11/14 13:28
 */
public interface AttachDao extends JpaRepository<Attach, Integer> {




}
