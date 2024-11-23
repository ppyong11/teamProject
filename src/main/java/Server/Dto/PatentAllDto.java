package Server.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class PatentAllDto {
    private String InventionName;
    private String InternationalpatentclassificationNumber;
    private String Abstract;
    private String RegistrationStatus;
    private String Applicant;
    private String RegistrationNumber;
    private LocalDate RegistrationDate;
    private String OpeningNumber;
    private LocalDate OpeningDate;
    private String ApplicationNumber;
    private LocalDate ApplicationDate;
    private String PublicNumber;
    private LocalDate PublicDate;
    private String DrawingPath;
    private String ThumbnailPath;

    public PatentAllDto(String title, String cpc, String summary, String applicant,
                        String regStatus, String appNumber, LocalDate appDate,String regNumber, LocalDate regDate, String openNumber,
                        LocalDate openDate, String pubNumber, LocalDate pubDate, String drawing, String thumbnail) {
        this.InventionName = title;
        this.InternationalpatentclassificationNumber = cpc;
        this.Abstract = summary;
        this.Applicant = applicant;
        this.RegistrationStatus = regStatus;
        this.ApplicationNumber = appNumber;
        this.ApplicationDate = appDate;
        this.RegistrationNumber = regNumber;
        this.RegistrationDate= regDate;
        this.OpeningNumber= openNumber;
        this.OpeningDate= openDate;
        this.PublicNumber= pubNumber;
        this.PublicDate = pubDate;
        this.DrawingPath = drawing;
        this.ThumbnailPath = thumbnail;
    }
}
