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
        query = "SELECT TIMEP, ID FROM POINT",
        resultSetMapping = "TimePointIdResult")
@SqlResultSetMapping(
        name = "TimePointIdResult",
        classes = @ConstructorResult(
                targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimePointId.class,
                columns = {
                        @ColumnResult(name = "timep"),
                        @ColumnResult(name = "id")
                }))
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {
    @EmbeddedId
    public TimePointId timePointId;

    public String point;

    @Column(length = 3000)
    public String message;

    public Point(TimePointId timePointId, String point, String message) {
        this.timePointId = timePointId;
        this.point = point;
        this.message = message;
    }
}
