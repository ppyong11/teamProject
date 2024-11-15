package Server.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatentDto {
    private String title;
    private String cpcCode;
    private String appNumber;
    private String applicant;

    public PatentDto(String title, String cpc, String appNumber, String applicant) {
        this.title = title;
        this.cpcCode = cpc;
        this.appNumber = appNumber;
        this.applicant = applicant;
    }
}
