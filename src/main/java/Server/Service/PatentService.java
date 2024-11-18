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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatentService {
    private PatentRepository patentRepository;
    private static final Logger logger= LoggerFactory.getLogger(PatentService.class);
    @Autowired
    public PatentService(PatentRepository patentRepository) {
        this.patentRepository = patentRepository;
    }

    // 검색 결과 저장 리스트
    List<PatentEntity> entities = new ArrayList<>();

    public List<PatentDto> findPatents(List<String> categories) {
        if (categories == null || categories.isEmpty() || categories.size() > 3) {
            // 카테고리 개수가 3개 이상이거나 리스트가 비어있으면 예외 던져
            throw new InvalidCategoryCountException("선택된 카테고리가 없거나 개수를 초과해 처리할 수 없습니다");
        }

        // 카테고리 크기에 따라 다르게 처리
        switch (categories.size()) {
            case 1:
                entities = patentRepository.findByCategory(categories.get(0));
                break;
            case 2:
                logger.info("Category 1: {}, Category 2: {}", categories.get(0), categories.get(1));
                entities = patentRepository.findBy2Categories(categories.get(0), categories.get(1));
                break;
            case 3:
                entities = patentRepository.findBy3Categories(categories.get(0), categories.get(1), categories.get(2));
                break;
        }

        if (categories.size() > 1) {
            // % 제거된 카테고리를 새 변수에 저장
            List<String> filteredCategories = categories.stream()
                    .map(category -> category.replace("%", "")) // % 제거
                    .collect(Collectors.toList());
            logger.info("Received categories with % appended: {}", filteredCategories);

            // 데이터 추가 필터링: applicationNumber가 같으면서 모든 카테고리가 포함된 데이터만
            Map<String, List<PatentEntity>> groupedByAppNumber = entities.stream()
                    .collect(Collectors.groupingBy(PatentEntity::getAppNumber));

            entities = groupedByAppNumber.values().stream()
                    .filter(group -> {
                        // 그룹에서 카테고리 추출
                        Set<String> groupCategories = group.stream()
                                .map(PatentEntity::getCpcCode)
                                .collect(Collectors.toSet());

                        // 검색한 모든 카테고리가 포함되어 있는지 확인 (교집합 조건)
                        return filteredCategories.stream()
                                .allMatch(category -> groupCategories.stream()
                                        .anyMatch(cpcCode -> cpcCode.contains(category)));
                    })
                    .flatMap(List::stream) // 필터링된 그룹의 모든 엔티티를 다시 평면화
                    .collect(Collectors.toList());
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


    //전체 데이터 조회
    public List<PatentAllDto> findAllData(String appNumber){
        //필요 repository, 로직 구현 필요
        if (appNumber == null || appNumber.trim().isEmpty()) {
            throw new InvalidCategoryCountException("선택된 데이터가 없습니다.");
        }
        entities= patentRepository.findByAppNumber(appNumber);
        // Entity -> DTO 변환

        return entities.stream()
                .map(entity -> new PatentAllDto(
                        entity.getTitle(),
                        entity.getCpcCode(),
                        entity.getSummary(),
                        entity.getApplicant(),
                        entity.getRegStatus(),
                        entity.getAppDate(),
                        entity.getAppNumber(),
                        entity.getRegDate(),
                        entity.getRegNumber(),
                        entity.getPubDate(),
                        entity.getDrawing(),
                        entity.getThumbnail())
                )
                .toList();

    }

    // 사용자 정의 예외 처리
    public class InvalidCategoryCountException extends RuntimeException {
        public InvalidCategoryCountException(String message) {
            super(message);
        }
    }

}
