package com.kenrui.retroanalyzer.database.compositekeys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class RepeatingCorrelationId implements Serializable {
    protected String id1;
    protected String id2;
    protected Integer repeats;

    public RepeatingCorrelationId(String id1, String id2, Integer repeats) {
        this.id1 = id1;
        this.id2 = id2;
        this.repeats = repeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RepeatingCorrelationId that = (RepeatingCorrelationId) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(repeats, that.repeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, repeats);
    }
}
