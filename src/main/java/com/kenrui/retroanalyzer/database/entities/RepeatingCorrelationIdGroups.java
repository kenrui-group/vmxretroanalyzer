package com.kenrui.retroanalyzer.database.entities;

import com.kenrui.retroanalyzer.database.compositekeys.RepeatingCorrelationId;
import com.kenrui.retroanalyzer.database.repositories.RepeatingCorrelationGroupsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RepeatingCorrelationIdGroups {
    @EmbeddedId
    public RepeatingCorrelationId repeatingCorrelationId;

    public RepeatingCorrelationIdGroups(RepeatingCorrelationId repeatingCorrelationId) {
        this.repeatingCorrelationId = repeatingCorrelationId;
    }
}
