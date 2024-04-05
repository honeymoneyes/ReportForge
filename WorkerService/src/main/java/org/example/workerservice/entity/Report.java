package org.example.workerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.workerservice.enums.ReportStatus;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "report", indexes = {
        @Index(name = "idx_unique_message_key", columnList = "phoneNumber, startDate, endDate")
})
public class Report implements Serializable {

    @Id
    private UUID uuid;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    private String reference;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Report report = (Report) o;
        return getUuid() != null && Objects.equals(getUuid(), report.getUuid());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
