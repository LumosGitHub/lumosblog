package lumosblog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lumosblog.model.entity.Contents;

/**
 * 扩展 分类与标签
 * @author 冠麟
 * @date 2019/11/2 16:00
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Content extends Contents {

    /**
     *  分类
     */
    String[] arrayCategories;

    /**
     * 标签
     */
    String[] arrayTags;

    public Content(Contents contents) {
        setCid(contents.getCid());
        setTitle(contents.getTitle());
        setSlug(contents.getSlug());
        setCreated(contents.getCreated());
        setModified(contents.getModified());
        setContent(contents.getContent());
        setAuthorId(contents.getAuthorId());
        setHits(contents.getHits());
        setType(contents.getType());
        setFmtType(contents.getFmtType());
        setThumbImg(contents.getThumbImg());
        setTags(contents.getTags());
        setCategories(contents.getCategories());
        setStatus(contents.getStatus());
        setCommentsNum(contents.getCommentsNum());
        setAllowComment(contents.getAllowComment());
        setAllowPing(contents.getAllowPing());
        setAllowFeed(contents.getAllowFeed());
        setUrl(contents.getUrl());
    }
}
