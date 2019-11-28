package lumosblog.service;

import lumosblog.model.dao.AttachDao;
import lumosblog.model.entity.Attach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 冠麟
 * @date 2019/11/14 13:29
 */
@Service
public class AttachService {


    @Autowired
    AttachDao dao;


    /**
     * 保存
     *
     * @param attach
     * @return
     */
    public Attach saveAttach(Attach attach) {
        return dao.save(attach);
    }

    /**
     * 分页查询
     *
     * @param pageable 分页对象
     * @return a {@link Page<Attach>}
     */
    public Page<Attach> getAttachAll(Pageable pageable) {
        return dao.findAll(pageable);
    }

    /**
     * 删除
     *
     * @param id 附件id
     */
    public void deleteAttach(Integer id) {
        dao.deleteById(id);
    }

    /**
     * 获取单条行数
     * @param id 行数id
     * @return {@link Optional<Attach>}
     */
    public Optional<Attach> getAttachById(Integer id) {
        return dao.findById(id);
    }


}
