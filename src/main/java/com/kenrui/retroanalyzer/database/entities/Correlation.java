package com.kenrui.retroanalyzer.database.entities;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;

//https://thoughts-on-java.org/result-set-mapping-constructor-result-mappings/
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForOneTickToMultipleQuotes",
                query = "SELECT TIME, ID1, ID2 FROM CORRELATION WHERE ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) > 1)",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForOneTickToOneQuote",
                query = "SELECT TIME, ID1, ID2 " +
                        "FROM CORRELATION " +
                        "WHERE ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) = 1) " +
                        "AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID2 HAVING COUNT(ID1) > 1)",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForDifferentTicksToSameQuote",
                query = "SELECT TIME, ID1, ID2 " +
                        "FROM CORRELATION " +
                        "WHERE ID2 IN (SELECT ID2 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID2 HAVING COUNT(ID1) > 1 AND (ID2 <> '' AND ID2 IS NOT NULL))",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForOneTickToNoQuote",
                query = "SELECT TIME, ID1, '' AS ID2 FROM CORRELATION WHERE POINT = 'POINT25' AND (ID2 = '' OR ID2 IS NULL)",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelatedPoints",
                query = "SELECT C.TIME TIME_C, C.ID1 ID1_C, C.ID2 ID2_C, P.TIME TIME_P, P.ID ID_P FROM CORRELATION C LEFT JOIN POINT P ON C.ID2 = P.ID AND (C.ID2 <> '' AND C.ID2 IS NOT NULL) AND C.POINT = 'POINT25' AND P.POINT = 'POINT5WA'",
                resultSetMapping = "CorrelatedResults")})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "TimeCorrelationIdResult",
                classes = @ConstructorResult(
                        targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId.class,
                        columns = {
                                @ColumnResult(name = "time"),
                                @ColumnResult(name = "id1"),
                                @ColumnResult(name = "id2")})),
        @SqlResultSetMapping(
                name = "CorrelatedResults",
                entities = {
                        @EntityResult(
                                entityClass = com.kenrui.retroanalyzer.database.entities.Correlation.class,
                                fields = {
                                        @FieldResult(name = "time", column = "TIME_C"),
                                        @FieldResult(name = "id1", column = "ID1_C"),
                                        @FieldResult(name = "id2", column = "ID2_C")
                                }
                        ),
                        @EntityResult(
                                entityClass = com.kenrui.retroanalyzer.database.entities.Point.class,
                                fields = {
                                        @FieldResult(name = "time", column = "TIME_P"),
                                        @FieldResult(name = "id", column = "ID_P")
                                }
                        )
                }
        )
})
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Correlation {
    @EmbeddedId
    public TimeCorrelationId timeCorrelationId;

    public String point;

    public String message;

    public Correlation(TimeCorrelationId timeCorrelationId, String point, String message) {
        this.timeCorrelationId = timeCorrelationId;
        this.point = point;
        this.message = message;
    }
}
