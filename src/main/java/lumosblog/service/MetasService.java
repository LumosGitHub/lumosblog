package lumosblog.service;

import lumosblog.model.dao.MetasDao;
import lumosblog.model.entity.Contents;
import lumosblog.model.entity.Metas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.spi.SyncResolver;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 冠麟
 * @date 2019/11/15 18:59
 */
@Service
public class MetasService {

    @Autowired
    private MetasDao dao;

    @Autowired
    private ContentsService contentsService;

    /**
     * 修改文章分类
     * 先寻找需要修改的分类名并修改 - 保存到 modify — 拼接时判断是否修改过 - 没修改不返回
     *
     * @param categories    原 分类名
     * @param modCategories 修改 分类名
     * @return 修改过的记录 {@link List<Contents>}
     */
    public List<Contents> modifyContentsCategories(String categories, String modCategories) {

        List<Contents> contentsList = contentsService.getContentsByType("post");
        List<Contents> contents = new ArrayList<>();
        boolean contentsState = false;
        for (Contents content : contentsList) {
            List<String> modify = new ArrayList<>();
            String[] value = content.getCategories().split(",");
            //修改
            for (int i = 0; i < value.length; i++) {
                if (value[i].equals(categories)) {
                    value[i] = modCategories;
                    contentsState = true;
                }
                modify.add(value[i]);
            }
            if (contentsState) {
                //拼接
                StringBuilder stringbuilder = new StringBuilder();
                modify.forEach(item -> {
                    String last = modify.get(modify.size() - 1);
                    if (item.equals(last)) {
                        stringbuilder.append(item);
                    } else {
                        stringbuilder.append(item);
                        stringbuilder.append(",");
                    }
                });
                content.setCategories(stringbuilder.toString());
                contents.add(content);
                contentsState = false;
            }
        }
        return contents;
    }

    /**
     * 统计 分类/标签 的使用情况
     *
     * @param metas 统计对象
     * @return {@link List<Metas>}
     */
    public List<Metas> getMetasCount(List<Metas> metas, String type) {

        List<Contents> contentsList = contentsService.getContentsByType("post");
        List<Metas> resultMetas = new ArrayList<>();
        for (Metas meta : metas) {
            int count = 0;
            for (Contents contents : contentsList) {
                String[] value = new String[]{};

                if ("tag".equals(type)) {
                    value = contents.getTags().split(",");
                }

                if ("category".equals(type)) {
                    value = contents.getCategories().split(",");
                }

                for (String s : value) {
                    if (s.equals(meta.getName())) {
                        count++;
                    }
                }
            }
            meta.setCount(count);
            resultMetas.add(meta);
        }
        return resultMetas;
    }


    /**
     * 按类型获取所有
     *
     * @return {@link List<Metas>}
     */
    public List<Metas> getMetasByType(String type) {
        return dao.findAllByType(type);
    }

    /**
     * 根据 id 与 类型查询单条
     *
     * @param id   行id
     * @param type 类型
     * @return {@link Metas}
     */
    public Metas getMetasByIdAndType(Integer id, String type) {
        return dao.findByMidAndType(id, type);
    }

    /**
     * 根据名字查询对应的记录
     *
     * @param name 名字
     * @param type 类型
     * @return {@link Metas}
     */
    public Metas getMetasByNameAndType(String name, String type) {
        return dao.findByNameAndTypeLike(name, type);
    }

    /**
     * 保存
     *
     * @param metas 分类标签对象
     * @return {@link Metas}
     */
    public Metas saveMetas(Metas metas) {
        return dao.save(metas);
    }

    /**
     * 删除
     *
     * @param id 行id
     */
    public void deleteMetas(Integer id) {
        dao.deleteById(id);
    }


}
