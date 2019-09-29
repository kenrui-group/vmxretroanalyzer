package com.kenrui.retroanalyzer.database.compositekeys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Getter
@Setter
public class TimeCorrelationId implements Serializable {
    protected String time;
    protected String id1;
    protected String id2;

    public TimeCorrelationId(String time, String id1, String id2) {
        this.time = time;
        this.id1 = id1;
        this.id2 = id2;
    }

    public TimeCorrelationId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TimeCorrelationId that = (TimeCorrelationId) o;
        return Objects.equals(time, that.time) && Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, id1, id2);
    }
}
