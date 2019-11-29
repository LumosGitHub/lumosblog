package lumosblog.service;

import lumosblog.model.dao.ContentsDao;
import lumosblog.model.entity.Archive;
import lumosblog.model.entity.Comments;
import lumosblog.model.entity.Contents;
import lumosblog.model.entity.Metas;
import lumosblog.utils.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 冠麟
 * @date 2019/10/7 17:51
 */
@Service
public class ContentsService {


    @Autowired
    ContentsDao dto;

    @Autowired
    MetasService metasService;


    /**
     * 根据 id 查询类型
     * @param cid 表id
     * @return String  “post” or ”page“
     */
    public String getContentsTypeByCid(Integer cid){
        return dto.findByCid(cid).getType();
    }

    /**
     * 根据文章类型查找
     *
     * @param type 类型
     * @return {@link List<Contents>}
     */
    public List<Contents> getContentsByType(String type) {
        return dto.findAllByType(type);
    }


    /**
     * 保存文章
     * 对文章标签进行分割保存
     *
     * @param contents 文章内容
     * @return 已保存的对象
     */
    public Contents save(Contents contents) {

        String tag = contents.getTags();
        String[] tags = null;
        if (!"".equals(tag) && tag != null) {
            tags = contents.getTags().split(",");
        }

        if (tags != null) {
            for (String s : tags) {
                Metas metas = metasService.getMetasByNameAndType(s, "tag");
                if (metas == null) {
                    metas = new Metas();
                    metas.setName(s);
                    metas.setType("tag");
                    metasService.saveMetas(metas);
                }

            }
        }
        return dto.save(contents);
    }

    /**
     * 根据标签与类型查询所有
     *
     * @param tags 标签
     * @param type 类型
     * @return {@link List<Contents>}
     */
    public List<Contents> getContentsByTagsAndType(String tags, String type) {
        return dto.findAllByTagsLikeAndType(tags, type);

    }


    /**
     * 根据id 获取单条文章
     *
     * @param id 文章id
     * @return 单个 Contents 对象
     */
    public Contents getContentsById(Integer id) {
        return dto.findByCid(id);
    }


    /**
     * 根据分页条件查询全部文章
     *
     * @param status   文章状态
     * @param pageable 分页参数
     * @return {@link Page<Contents>}
     */
    public Page<Contents> getContentsByStatusPage(String status, String type, Pageable pageable) {
        return dto.findAllByStatusAndType(status, type, pageable);
    }

    /**
     * ·文章管理·
     * 根据条件获取文章
     *
     * @param title      文章标题
     * @param categories 分类
     * @param status     状态
     * @param type       类型
     * @param pageable   分页参数
     * @return {@link Page<Contents>}
     */
    public Page<Contents> getContentsByTitleLikeAndCategoriesLikeAndStatusLikeAndType(String title, String categories, String status, String type, Pageable pageable) {
        return dto.findAllByTitleLikeAndCategoriesLikeAndStatusLikeAndType("%" + title + "%", "%" + categories + "%", "%" + status + "%", type, pageable);
    }


    /**
     * ·页面管理·
     * 查询页面类型
     *
     * @param type 类型
     * @return {@link Page<Contents>}
     */
    public Page<Contents> getContentsByType(String type, Pageable pageable) {
        return dto.findAllByType(type, pageable);
    }

    /**
     * 删除文章
     *
     * @param cid 文章id
     */
    public void deleteContent(Integer cid) {
        dto.deleteById(cid);
    }

    /**
     * 获取所有文章
     *
     * @param status 文章状态
     * @return List<Contents> {@link Contents}
     */
    public List<Contents> getContentsByStatus(String status) {
        return dto.findAllByStatus(status);
    }

    /**
     * 获取最新文章
     *
     * @param status 文章状态
     * @return 10条最新文章
     */
    public List<Contents> getNewestContents(String status,String type) {
        return dto.findFirst10ByStatusAndTypeOrderByCidDesc(status,type);
    }

    /**
     * 查询文章的创建时间并进行归档
     *
     * @return {@link Archive}
     */
    public List<Archive> getArchivesCount() {
        List<Object[]> list = dto.getArchivesCount();
        List<Archive> archives = new ArrayList<>();
        for (Object[] objects : list) {
            Archive archive = new Archive();
            archive.setDateFiling(objects[0].toString());
            archive.setCount(objects[1].toString());
            archives.add(archive);
        }
        if (archives != null) {
            return archives.stream().map(this::parseArchive).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * 根据传递的月份进行装配
     *
     * @param archive 传递的参数
     * @return 已装配好对象
     */
    private Archive parseArchive(Archive archive) {
        String dateStr = archive.getDateFiling();
        Date sd = DateKit.toDate(dateStr + "01", "yyyy年MM月dd");
        archive.setDate(sd);
        int start = DateKit.toUnix(sd);
        Calendar calender = Calendar.getInstance();
        calender.setTime(sd);
        calender.add(Calendar.MONTH, 1);
        Date endSd = calender.getTime();
        int end = DateKit.toUnix(endSd) - 1;
        List<Contents> contents = dto.getArchives("post", "publish", start, end);
        archive.setArticles(contents);
        return archive;
    }

    /**
     * 模糊查询 文章标题
     *
     * @param title 文章标题
     * @return List<Contents>
     */
    public List<Contents> searchTitle(String status, String type, String title) {
        return dto.findAllByStatusAndTypeAndTitleLike(status, type, "%" + title + "%");
    }

    /**
     * 查询标签
     *
     * @param status 状态
     * @param tags   标签
     * @return List<Contents> {@link Contents}
     */
    public List<Contents> searchTags(String status, String type, String tags) {
        return dto.findAllByStatusAndTypeAndTagsLike(status, type, tags);
    }


    /**
     * 查询分类
     *
     * @param status     状态
     * @param categories 分类
     * @return List<Contents> {@link Contents}
     */
    public List<Contents> searchCategories(String status, String type, String categories) {
        return dto.findAllByStatusAndTypeAndCategoriesLike(status, type, categories);
    }


    /**
     * 查询自定义页面地址
     *
     * @param status 状态
     * @param slug   查询地址
     * @return {@link Contents}
     */
    public Contents getCustomize(String status, String type, String slug) {
        return dto.findByStatusAndTypeAndSlug(status, type, slug);
    }


}
