package Server.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatentDto {
    private String InventionName;
    private String InternationalpatentclassificationNumber;
    private String Abstract;
    private String ApplicationNumber;
    private String Applicant;

    public PatentDto(String title, String cpc, String summary, String appNumber, String applicant) {
        this.InventionName = title;
        this.InternationalpatentclassificationNumber = cpc;
        this.Abstract= summary;
        this.ApplicationNumber = appNumber;
        this.Applicant = applicant;
    }
}
