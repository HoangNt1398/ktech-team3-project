package com.example.TeamPj.member.entity;

import ch.qos.logback.core.status.Status;
import com.example.TeamPj.tags.Tags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.metrics.StartupStep;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_table")
public class Member {
    @Column(unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String image;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE ;
   public enum Status{
       ACTIVE, INACTIVE
    }

    private Integer favorite_count;
    private Integer created_count;
    private Integer recorde_count;
    private boolean isVoted;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tags tags;


    private LocalDateTime deletionDate;
    private LocalDateTime createAt;
    private LocalDateTime lastModified;

}
