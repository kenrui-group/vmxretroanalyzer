package com.kenrui.retroanalyzer.database.entities;

import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@NamedNativeQuery(
        name = "Point.findPointIds",
        query = "SELECT TIME, ID FROM POINT",
        resultSetMapping = "TimePointIdResult")
@SqlResultSetMapping(
        name = "TimePointIdResult",
        classes = @ConstructorResult(
                targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimePointId.class,
                columns = {
                        @ColumnResult(name = "time"),
                        @ColumnResult(name = "id")
                }))
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @EmbeddedId
    public TimePointId timePointId;

    public String message;
}
