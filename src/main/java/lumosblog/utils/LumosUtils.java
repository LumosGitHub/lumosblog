package lumosblog.utils;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedOutput;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lumosblog.controller.admin.AdminApiController;
import lumosblog.model.entity.Contents;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.context.Theme;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author 冠麟
 * @date 2019/11/3 11:08
 */
@Slf4j
@Configuration
public class LumosUtils {


    /**
     * 获取RSS输出
     */
    public static String getRssXml(java.util.List<Contents> articles) throws FeedException {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle("Lumos");
        channel.setLink(SettingUtils.SOURCEIP);
        channel.setDescription("Lumos Blog");
        channel.setLanguage("zh-CN");
        java.util.List<Item> items = new ArrayList<>();
        articles.forEach(post -> {
            Item item = new Item();
            item.setTitle(post.getTitle());
            Content content = new Content();
            String value = mdToHtml(post.getContent());

            char[] xmlChar = value.toCharArray();
            for (int i = 0; i < xmlChar.length; ++i) {
                if (xmlChar[i] > 0xFFFD) {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                } else if (xmlChar[i] < 0x20 && xmlChar[i] != 't' & xmlChar[i] != 'n' & xmlChar[i] != 'r') {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                }
            }

            value = new String(xmlChar);

            content.setValue(value);
            item.setContent(content);

            String url = "";
            if (post.getSlug() == null || post.getSlug().isEmpty()) {
                url = post.getCid().toString();
            } else {
                url = post.getSlug();
            }

            item.setLink(SettingUtils.SOURCEIP + "/article/" + url);
            item.setPubDate(DateKit.toDate(post.getCreated()));
            items.add(item);
        });
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }


    /**
     * 显示文章内容，转换markdown为html
     *
     * @param
     * @return public static String article(String value) {
     * if (StringKit.isNotBlank(value)) {
     * value = value.replace("<!--more-->", "\r\n");
     * return TaleUtils.mdToHtml(value);
     * }
     * return "";
     * }
     */


    static class LinkAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof org.commonmark.node.Image) {
                attributes.put("title", attributes.get("alt"));
            }
        }
    }


    /**
     * markdown转换为html
     */
    public static String mdToHtml(String markdown) {

        List<Extension> extensions = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .attributeProviderFactory(context -> new LinkAttributeProvider())
                .extensions(extensions).build();

        String content = renderer.render(document);

        content = emoji(content);

       /* // 支持网易云音乐输出
        if (TaleConst.BCONF.getBoolean(ENV_SUPPORT_163_MUSIC, true) && content.contains(MP3_PREFIX)) {
            content = content.replaceAll(MUSIC_REG_PATTERN, MUSIC_IFRAME);
        }
        // 支持gist代码输出
        if (TaleConst.BCONF.getBoolean(ENV_SUPPORT_GIST, true) && content.contains(GIST_PREFIX_URL)) {
            content = content.replaceAll(GIST_REG_PATTERN, GIST_REPLATE_PATTERN);
        }
        */

        return content;
    }


    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }


    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     * @return
     */
    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return htmlToText(mdToHtml(html));
        } else {
            String text = htmlToText(mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }

    /**
     * 提取html中的文字
     */
    public static String htmlToText(String html) {
        if (html!=null) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }


    /**
     * 截取文章摘要(返回HTML)
     *
     * @param value 文章内容
     * @return 转换 markdown 为 html
     */
    public static String intro(String value) {
        if (value == null) {
            return "";
        }
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return mdToHtml(html);
        } else {
            return mdToHtml(value);
        }
    }



    public static final String CLASSPATH = SettingUtils.UPLOAD_FILE;
    public static final String UP_DIR = CLASSPATH.substring(0, CLASSPATH.length() - 1);

    public static String getFileKey(String name) {
        String prefix = "/upload/" + DateKit.toString(new Date(), "yyyy/MM");
        String dir = UP_DIR + prefix;
        log.info(dir);
        if (!Files.exists(Paths.get(dir))) {
            new File(dir).mkdirs();
        }
        return prefix + "/" + UUID.randomUUID().toString().replace("-", "") + "." + fileExt(name);
    }


    public static String fileExt(String fname) {
        if ("".equals(fname) || fname == null || fname.indexOf('.') == -1) {
            return null;
        }
        return fname.substring(fname.lastIndexOf('.') + 1);
    }


}
