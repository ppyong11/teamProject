package Server.Service;

import Server.Controller.PatentController;
import Server.Dto.PatentAllDto;
import Server.Entity.PatentEntity;
import Server.Repository.PatentRepository;
import Server.Dto.PatentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatentService {
    private PatentRepository patentRepository;
    private static final Logger logger= LoggerFactory.getLogger(PatentService.class);
    Pageable pageable;

    @Autowired
    // 첫 페이지, 1000개 제한
    public PatentService(PatentRepository patentRepository) {
        this.patentRepository = patentRepository;
        pageable = PageRequest.of(0, 500);
    }

    public List<PatentDto> findPatents(List<String> categories) {
        // 검색 결과 저장 리스트
        List<PatentEntity> entities = new ArrayList<>();

        if (categories == null || categories.isEmpty() || categories.size() > 3) {
            // 카테고리 개수가 3개 이상이거나 리스트가 비어있으면 예외 던져
            throw new InvalidCategoryCountException("선택된 카테고리가 없거나 개수를 초과해 처리할 수 없습니다");
        }

        // 카테고리 크기에 따라 다르게 처리
        switch (categories.size()) {
            case 1:
                entities = patentRepository.findByCategory(categories.get(0), pageable);
                break;
            case 2:
                logger.info("Category 1: {}, Category 2: {}", categories.get(0), categories.get(1));
                entities = patentRepository.findBy2Categories(categories.get(0), categories.get(1), pageable);
                break;
            case 3:
                entities = patentRepository.findBy3Categories(categories.get(0), categories.get(1), categories.get(2), pageable);
                break;
        }

        // Entity -> DTO 변환
        return entities.stream()
                .map(entity -> new PatentDto(
                        entity.getInventionName(),
                        entity.getInternationalpatentclassificationNumber(),
                        entity.getAbstract(),
                        entity.getApplicationNumber(),
                        entity.getApplicant()
                ))
                .toList();
    }

    //전체 데이터 조회
    public List<PatentAllDto> findAllData(String appNumber){
        //전체 데이터 담는 리스트
        List<PatentEntity> allEntities = new ArrayList<>();

        //필요 repository, 로직 구현 필요
        if (appNumber == null || appNumber.trim().isEmpty()) {
            throw new InvalidCategoryCountException("선택된 데이터가 없습니다.");
        }
        logger.info("Service: appNumber= {}", appNumber);

        allEntities= patentRepository.findByAppNumber(appNumber);

        // Entity -> DTO 변환
        return allEntities.stream()
                .map(entity -> new PatentAllDto(
                        entity.getInventionName(),
                        entity.getInternationalpatentclassificationNumber(),
                        entity.getAbstract(),
                        entity.getApplicant(),
                        entity.getRegistrationStatus(),
                        entity.getApplicationNumber(),
                        entity.getApplicationDate(),
                        entity.getRegistrationNumber(),
                        entity.getRegistrationDate(),
                        entity.getOpeningNumber(),
                        entity.getOpeningDate(),
                        entity.getPublicNumber(),
                        entity.getPublicDate(),
                        entity.getDrawingPath()

                ))
                .toList();

    }

    // 사용자 정의 예외 처리
    public class InvalidCategoryCountException extends RuntimeException {
        public InvalidCategoryCountException(String message) {
            super(message);
        }
    }

}
