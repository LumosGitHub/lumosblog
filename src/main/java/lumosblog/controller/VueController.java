package lumosblog.controller;

import lumosblog.model.entity.VueEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 冠麟
 * @date 2019/9/15 14:48
 */
@Controller
public class VueController {


    @RequestMapping("Vue")
    public String initialize() {
        return "Vue";
    }

    @RequestMapping("/list")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<VueEntity> vueEntityList() {
        List<VueEntity> list = new LinkedList<>();
        VueEntity vueEntity = new VueEntity(3, "name3", "pass3");
        VueEntity vueEntity1 = new VueEntity(4, "name4", "pass4");
        VueEntity vueEntity2 = new VueEntity(5, "name5", "pass5");
        list.add(vueEntity);
        list.add(vueEntity1);
        list.add(vueEntity2);
        return list;
    }


}
