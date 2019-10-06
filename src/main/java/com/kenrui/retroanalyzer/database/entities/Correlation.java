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
                name = "Correlation.findCorrelationIdsNotInPoint",
                query = "SELECT * FROM CORRELATION WHERE ID2 NOT IN (SELECT ID FROM POINT) AND ID2 <> '' AND ID2 IS NOT NULL",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.correlatePoints",
                query = "SELECT C.TIME AS TIME1, C.ID1 AS ID1_C, C.ID2 AS ID2_C, P.TIMEP AS TIMEP, P.ID AS ID_P FROM CORRELATION C LEFT JOIN POINT P ON C.ID2 = P.ID WHERE (C.ID2 <> '' AND C.ID2 IS NOT NULL) AND C.POINT = 'POINT25'",
                resultSetMapping = "CorrelationResults"),
        @NamedNativeQuery(
                name = "Correlation.correlatedPoints",
                query = "SELECT C.TIME AS TIME1, C.ID1 AS ID1_C, C.ID2 AS ID2_C, P.TIMEP AS TIMEP, P.ID AS ID_P FROM CORRELATION C INNER JOIN POINT P ON C.ID2 = P.ID WHERE (C.ID2 <> '' AND C.ID2 IS NOT NULL) AND C.POINT = 'POINT25'",
                resultSetMapping = "CorrelationResults"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelations",
                query = "SELECT C.TIME AS TIME_C, C.ID1 AS ID1_C, C.ID2 AS ID2_C FROM CORRELATION C ",
                resultSetMapping = "TimeCorrelationIdResult")
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "TimeCorrelationIdResult",
                classes = @ConstructorResult(
                        targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId.class,
                        columns = {
                                @ColumnResult(name = "time"),
                                @ColumnResult(name = "id1"),
                                @ColumnResult(name = "id2")
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "CorrelationResults",
                classes = {
                        @ConstructorResult(
                                targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId.class,
                                columns = {
                                        @ColumnResult(name = "time"),
                                        @ColumnResult(name = "id1"),
                                        @ColumnResult(name = "id2")
                                }),
                        @ConstructorResult(
                                targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimePointId.class,
                                columns = {
                                        @ColumnResult(name = "timep"),
                                        @ColumnResult(name = "id")
                                })
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

    @Column(length = 3000)
    public String message;

    public Correlation(TimeCorrelationId timeCorrelationId, String point, String message) {
        this.timeCorrelationId = timeCorrelationId;
        this.point = point;
        this.message = message;
    }
}
