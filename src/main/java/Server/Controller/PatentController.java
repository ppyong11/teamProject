package Server.Controller;

import Server.Dto.PatentAllDto;
import Server.Dto.PatentDto;
import Server.Service.PatentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patents")
public class PatentController {
    private static final Logger logger= LoggerFactory.getLogger(PatentController.class);
    @Autowired
    private PatentService patentService;

    //cpc 데이터 조회
    @GetMapping("/category")
    public List<PatentDto> getPetents(@RequestParam String categories){
        // categories 문자열을 ,로 분리하여 List<String>으로 변환 후 검색을 위해 % 추가
        String trim_categories= categories.replace(" ", "");
        List<String> categoryList = Arrays.asList(trim_categories.split(","));

        List<String> modifiedCategoryList = categoryList.stream()
                .map(category -> category + "%")
                .collect(Collectors.toList());

        logger.info("Received categories with % appended: {}", modifiedCategoryList);
        return patentService.findPatents(modifiedCategoryList);
    }

    //어플리케이션 번호로 조회
    @GetMapping("/appNumber")
    public List<PatentAllDto> getAllDate(@RequestParam String number){
        logger.info("Received categories with % appended: {}", number);
        return patentService.findAllData(number);
    }
}
