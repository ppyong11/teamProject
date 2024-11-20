package Server.Entity;
import Server.Dto.*;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patent")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class PatentEntity {
    @Id
    private String ApplicationNumber;

    @Column(nullable = false)
    private String InventionName;

    @Column(nullable = false)
    private String InternationalpatentclassificationNumber;

    @Column(nullable = false)
    private String Abstract;

    private String RegistrationStatus;

    private String Applicant;

    private Date RegistrationDate;

    private String RegistrationNumber;

    private Date ApplicationDate;

    private String OpeningNumber;

    private Date OpeningDate;

    private String PublicNumber;

    private Date PublicDate;

    @Column(columnDefinition = "varchar(255) default '이미지 정보 없음'")
    private String DrawingPath;

    @Column(columnDefinition = "varchar(255) default '이미지 정보 없음'")
    private String ThumbnailPath;
}
