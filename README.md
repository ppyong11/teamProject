## 📌 프로젝트 소개
1. **프로젝트명**: 특허 데이터 관계 시각화 웹
2. **프로젝트 인원**: 4명 (프론트엔드 2명, 백엔드 2명)
3. **프로젝트 기간**: 2024.09~2024.11
4. **프로젝트 목표**: 특허 데이터 기반의 2D, 3D 지식 그래프를 구축하고 이를 시각화하여 사용자가 방대한 특허 데이터를 직관적으로 이해하고 효율적으로 의사결정할 수 있도록 지원합니다.
5. **사용 기술**
    - **Language:** Java
    - **Framework:** Spring Boot
    - **Database:** MariaDB
    - **API:** RESTful API, (외부) KIPRIS API
## 📌 담당 역할 및 구현 내용
### 대량 데이터 DB 적재
<img width="469" height="217" alt="image (1)" src="https://github.com/user-attachments/assets/3b1a94b8-3502-467c-86bc-fa9f8801d2d3" /><br>
KIPRIS API로 수집한 특허 데이터를 배치 처리(JDBC Batch) 방식으로 적재해 대량 데이터 처리 성능을 개선했으며 특허가 여러 CPC 코드를 가지는 특성을 고려해 출원번호를 기준으로 중복 데이터를 방지하도록 설계했습니다.
### RESTful API 개발
<img width="954" height="608" alt="image (2)" src="https://github.com/user-attachments/assets/5a2f3b3b-82cc-4131-9fb2-67c6967034a9" /><br>
교집합 검색 기능을 지원하기 위해 JPQL 기반 정적 쿼리 분기 구조를 구현하고, 결과 데이터는 Pageable로 제한해 조회 성능을 최적화했습니다.

## 📌 주요 기능 UI 소개
### **트리 구조 검색** <br/>
  <img width="763" src="https://github.com/user-attachments/assets/6e4fc5a7-a65c-4931-95b8-190e8f8608a2"/>    <br/> 
  CPC 코드를 이용한 트리 구조로 검색이 가능하며 관련 있는 노드별로 깊이 있는 검색을 지원합니다.     <br/> <br/> 
  <img width="763" src="https://github.com/user-attachments/assets/57e9ea76-096f-4644-adaf-638e64683545"/>    <br/> 
  교집합 검색으로 통해 동일한 CPC 코드를 가지는 특허 데이터를 알 수 있으며 CPC 코드는 3개까지 지정 가능합니다. <br/>  <br/> 
### **특정 단어 검색** <br/>
<img width="763" src="https://github.com/user-attachments/assets/2b796d99-1d23-4587-9672-51b0425e7404"/>    <br/> 
특허 데이터의 키워드를 입력해 검색이 가능합니다.
### **특허 데이터 검색 및 결과** <br/>
지식 그래프 형태는 데이터 간 관계를 이해하는 데에 용이하며, 테이블 형태는 데이터를 직관적이고 깔끔하게 확인 가능합니다. 각 UI가 제공하는 검색 결과 특허 데이터와 상세 정보는 동일합니다. <br/><br/>
**[3D 지식 그래프]**<br/>
<img width="765" alt="3D 검색 결과" src="https://github.com/user-attachments/assets/f80428b6-22ed-440c-8012-e5fecb95b25c" /><br/> <br/>

**[2D 지식 그래프]**<br/>
<img width="764" alt="2D 검색 결과" src="https://github.com/user-attachments/assets/c5567b27-d1e9-45f6-9a1f-6793ce5e1d3c" /> <br/> <br/>

**[테이블]**<br/>
<img width="763" alt="테이블 검색 결과" src="https://github.com/user-attachments/assets/0e53514d-8aa4-4114-b6be-4a666a417826" /> <br/> <br/>

## 📝 프로젝트 상세 내용
  - **노션 링크**  
https://www.notion.so/32a516e994b3816ba2b9fe820f8a8b76?source=copy_link
- **영상 링크**  
 https://drive.google.com/file/d/1IziAaRZ3RMDwySsjdx2uOZelAvvZFFK6/view?usp=sharing

