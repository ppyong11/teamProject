package Server.Service;

import Server.Controller.PatentController;
import Server.Dto.PatentAllDto;
import Server.Entity.PatentEntity;
import Server.Repository.PatentRepository;
import Server.Dto.PatentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatentService {
    private PatentRepository patentRepository;
    private static final Logger logger= LoggerFactory.getLogger(PatentService.class);
    @Autowired
    public PatentService(PatentRepository patentRepository) {
        this.patentRepository = patentRepository;
    }

    public List<PatentDto> findPatents(List<String> categories) {
        if (categories == null || categories.isEmpty() || categories.size() > 3) {
            // 카테고리 개수가 3개 이상이거나 리스트가 비어있으면 예외 던져
            throw new InvalidCategoryCountException("선택된 카테고리가 없거나 개수를 초과해 처리할 수 없습니다");
        }

        // 검색 결과 저장 리스트
        List<PatentEntity> entities = new ArrayList<>();

        // 카테고리 크기에 따라 다르게 처리
        switch (categories.size()) {
            case 1:
                entities = patentRepository.findByCategory(categories.get(0));
                break;
            case 2:
                entities = patentRepository.findBy2Categories(categories.get(0), categories.get(1));
                break;
            case 3:
                entities = patentRepository.findBy3Categories(categories.get(0), categories.get(1), categories.get(2));
                break;
        }

        // Entity -> DTO 변환
        return entities.stream()
                .map(entity -> new PatentDto(
                        entity.getTitle(),
                        entity.getCpcCode(),
                        entity.getAppNumber(),
                        entity.getApplicant()
                ))
                .toList();
    }

    /*
    //전체 데이터 조회
    public List<PatentAllDto> findAllDate(String appNumber){
        //필요 repository, 로직 구현 필요
    }*/

    // 사용자 정의 예외 처리
    public class InvalidCategoryCountException extends RuntimeException {
        public InvalidCategoryCountException(String message) {
            super(message);
        }
    }

}
