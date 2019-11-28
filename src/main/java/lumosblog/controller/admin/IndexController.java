package lumosblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import lumosblog.model.dto.Statistics;
import lumosblog.model.entity.*;
import lumosblog.service.AttachService;
import lumosblog.service.CommentsService;
import lumosblog.service.ContentsService;
import lumosblog.utils.LumosUtils;
import lumosblog.utils.RestReturns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 冠麟
 * @date 2019/9/11 11:01
 */
@Slf4j
@Controller
public class IndexController {


    @Autowired
    ContentsService contentsService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    AttachService attachService;



    @GetMapping("/admin/index")
    public String index() {
        return "/admin/index.html";
    }


    /**
     * 仪表盘 5条 最新文章与 最新留言
     *
     * @return list []    0是 最新文章   1是最新留言
     */
    @GetMapping("/admin/api/indexNewest")
    @ResponseBody
    public List[] indexNewest() {
        List<Contents> contentsList = contentsService.getNewestContents("publish").subList(0, 5);
        List<Comments> commentsList = commentsService.getNewestComment5();
        return new List[]{contentsList, commentsList};
    }

    /**
     * 仪表盘
     *
     * @return
     */
    @GetMapping("/admin/index/init")
    @ResponseBody
    public Statistics reqSta() {

        int conCount = contentsService.getContentsByStatus("publish").size();

        int comCount = commentsService.getByIdAll().size();

        Statistics statistics = new Statistics();
        statistics.setArticles(conCount);
        statistics.setComments(comCount);
        //附件
        statistics.setAttachs(7);
        return statistics;

    }


    /**
     * 修改评论状态
     *
     * @return
     */
    @PostMapping("/admin/api/comment/status")
    @ResponseBody
    public RestReturns updateStatus(@RequestBody Comments comments) {
        int result = commentsService.updateComment(comments.getCoid(), comments.getStatus());
        if (result == 1) {
            log.info("修改评论状态成功！" + comments.getCid());
            return RestReturns.ok();
        } else {
            log.info("修改评论状态失败！" + comments.getCid());
            return RestReturns.fail("修改评论失败！");
        }

    }


    /**
     * 附件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/admin/api/attach/upload")
    @ResponseBody
    public RestReturns upload(@RequestParam("file") MultipartFile[] file)  {

        try {
        for (MultipartFile multipartFile : file) {

            String fkey=LumosUtils.getFileKey(multipartFile.getOriginalFilename());
            String filePath=LumosUtils.UP_DIR+fkey;
            log.info(fkey+"   fkey");
            log.info(filePath+"   filePath");



            File file1=new File(filePath);
            multipartFile.transferTo(file1);
            Attach attach=new Attach();
            attach.setFname(multipartFile.getOriginalFilename());
            attach.setFkey(fkey.substring(7));
            attach.setFtype(multipartFile.getContentType().substring(0,5));
            attach.setCreated((int) (System.currentTimeMillis()/1000));
            attachService.saveAttach(attach);

        }

        } catch (Exception e) {
            log.error(e.toString());
            return RestReturns.fail("上传失败！");
        }

        return RestReturns.ok();
    }


    /**
     * 系统日志
     *
     * @return
     */
    @GetMapping("/admin/api/logs")
    @ResponseBody
    public RestReturns sysLogs() {
        System.out.println("sysLogs*****");
        /**
         * Page(pageNum=1, limit=8, prevPage=1, nextPage=2, totalPages=3, totalRows=20, rows=[Logs(id=20, action=登录后台,
         *  * data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568680477),
         *  * Logs(id=19, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568547956),
         *  * Logs(id=18, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568547887),
         *  * Logs(id=17, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568547807),
         *  * Logs(id=16, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568529588),
         *  * Logs(id=15, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568529448),
         *  * Logs(id=14, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568456527),
         *  * Logs(id=13, action=登录后台, data=username=qqqqqq&password=qqqqqq, authorId=4, ip=27.0.0.1, created=1568249212)],
         *  * isFirstPage=true, isLastPage=false, hasPrevPage=false, hasNextPage=true, navPages=8, navPageNums=[1, 2, 3]),
         *  * success=true, msg=null, code=0, timestamp=1568680478)
         */
        Page<Logs> page = new Page<>();
        Logs logs = new Logs();
        Logs logs1 = new Logs();
        Logs logs2 = new Logs();
        Logs logs3 = new Logs();
        Logs logs4 = new Logs();
        Logs logs5 = new Logs();
        Logs logs6 = new Logs();
        Logs logs7 = new Logs();
        logs.setId(20);
        logs.setAction("登录后台");
        logs.setData("username=qqqqqq&password=qqqqqq");
        logs.setAuthorId(4);
        logs.setIp("27.0.0.1");
        logs.setCreated(1568680477);


        logs1.setId(19);
        logs1.setAction("登录后台");
        logs1.setData("username=qqqqqq&password=qqqqqq");
        logs1.setAuthorId(4);
        logs1.setIp("27.0.0.1");
        logs1.setCreated(1568547956);

        logs2.setId(18);
        logs2.setAction("登录后台");
        logs2.setData("username=qqqqqq&password=qqqqqq");
        logs2.setAuthorId(4);
        logs2.setIp("27.0.0.1");
        logs2.setCreated(1568547956);

        logs3.setId(17);
        logs3.setAction("登录后台");
        logs3.setData("username=qqqqqq&password=qqqqqq");
        logs3.setAuthorId(4);
        logs3.setIp("27.0.0.1");
        logs3.setCreated(1568547956);


        logs4.setId(16);
        logs4.setAction("登录后台");
        logs4.setData("username=qqqqqq&password=qqqqqq");
        logs4.setAuthorId(4);
        logs4.setIp("27.0.0.1");
        logs4.setCreated(1568547956);


        logs5.setId(15);
        logs5.setAction("登录后台");
        logs5.setData("username=qqqqqq&password=qqqqqq");
        logs5.setAuthorId(4);
        logs5.setIp("27.0.0.1");
        logs5.setCreated(1568547956);

        logs6.setId(14);
        logs6.setAction("登录后台");
        logs6.setData("username=qqqqqq&password=qqqqqq");
        logs6.setAuthorId(4);
        logs6.setIp("27.0.0.1");
        logs6.setCreated(1568547956);

        logs7.setId(13);
        logs7.setAction("登录后台");
        logs7.setData("username=qqqqqq&password=qqqqqq");
        logs7.setAuthorId(4);
        logs7.setIp("27.0.0.1");
        logs7.setCreated(1568547956);


        page.setPageNum(1);
        page.setLimit(8);
        page.setPrevPage(1);
        page.setNextPage(2);
        page.setTotalPages(3);
        page.setTotalRows(20);

        List<Logs> list = new ArrayList<>();
        list.add(logs);
        list.add(logs1);
        list.add(logs2);
        list.add(logs3);
        list.add(logs4);
        list.add(logs5);
        list.add(logs6);
        list.add(logs7);

        page.setRows(list);

        //page.setFirstPage(true);
        //page.setLastPage(false);
        page.setHasPrevPage(false);
        page.setHasNextPage(true);
        page.setNavPages(8);
        page.setNavPageNums(new Integer[]{1, 2, 3});
        page.setSuccess(true);
        page.setMsg(null);
        page.setCode(0);
        page.setTimestamp(1568680478);
        RestReturns restReturns = RestReturns.ok(page);

        return restReturns;
    }


}
