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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "cpc_code", nullable = false, unique = true)
    private String cpcCode;

    @Column(name = "abstract", nullable = false, unique = true)
    private String summary;

    @Column(name = "reg_status")
    private String regStatus;

    @Column(name = "applicant")
    private String applicant;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "app_date")
    private Date appDate;

    @Column(name = "app_number")
    private String appNumber;

    @Column(name = "pub_date")
    private Date pubDate;

    @Column(name = "pub_number")
    private String pubNumber;

    @Column(name = "drawing_path")
    private String drawing;

    @Column(name = "Thumbnail_path")
    private String thumbnail;
}
