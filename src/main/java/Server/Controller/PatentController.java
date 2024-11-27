package Server.Controller;

import Server.Dto.PatentAllDto;
import Server.Dto.PatentDto;
import Server.Entity.PatentEntity;
import Server.Service.PatentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/patents")
public class PatentController {
    private static final Logger logger= LoggerFactory.getLogger(PatentController.class);
    @Autowired
    private PatentService patentService;

    //cpc 데이터 조회
//    @GetMapping("/category")
    @RequestMapping("/category")
    public List<PatentDto> getPetents(@RequestParam String categories){
        // categories 문자열을 ,로 분리하여 List<String>에 저장
        String trim_categories= categories.replace(" ", "");
        List<String> categoryList = Arrays.asList(trim_categories.split(","));

        logger.info("Received categories with % appended: {}", categoryList);
        return patentService.findPatents(categoryList);
    }

    //어플리케이션 번호로 조회
    @GetMapping("/appNumber")
    public List<PatentAllDto> getAllDate(@RequestParam String number){
        String trim_number= number.trim();
        logger.info("공백 제거: {}", trim_number);
        return patentService.findAllData(trim_number);
    }
}