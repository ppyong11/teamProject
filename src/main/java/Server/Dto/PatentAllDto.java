package Server.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatentAllDto {
    private String title;
    private String cpcCode;
    private String summary;
    private String regStatus;
    private String applicant;
    private Date regDate;
    private String regNumber;
    private Date appDate;
    private String appNumber;
    private Date pubDate;
    private String drawing;
    private String thumbnail;

    public PatentAllDto(String title, String cpc, String summary, String applicant,
                        String regStatus, Date appDate, String appNumber, Date regDate, String regNumber,
                        Date pubDate, String drawing, String thumbnail) {
        this.title = title;
        this.cpcCode = cpc;
        this.summary = summary;
        this.applicant = applicant;
        this.regStatus = regStatus;
        this.appDate = appDate;
        this.appNumber = appNumber;
        this.regDate= regDate;
        this.regNumber = regNumber;
        this.pubDate = pubDate;
        this.drawing = drawing;
        this.thumbnail = thumbnail;
    }
}
