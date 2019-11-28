package lumosblog.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author 冠麟
 * @date 2019/10/19 22:22
 */
@Data
public class Archive {

    private String dateFiling;
    private Date date;
    private String count;
    private List<Contents> articles;

    public Archive(String dateFiling, String count) {
        this.dateFiling = dateFiling;
        this.count = count;

    }

    public Archive() {
    }

    public Archive(String dateFiling, Date date, String count, List<Contents> articles) {
        this.dateFiling = dateFiling;
        this.date = date;
        this.count = count;
        this.articles = articles;
    }
}
