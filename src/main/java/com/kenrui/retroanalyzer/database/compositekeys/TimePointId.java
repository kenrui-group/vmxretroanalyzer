package com.kenrui.retroanalyzer.database.compositekeys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class TimePointId implements Serializable {
    protected String timep;
    protected String id;

    public TimePointId(String timep, String id) {
        this.timep = timep;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TimePointId that = (TimePointId) o;
        return Objects.equals(timep, that.timep) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timep, id);
    }
}
