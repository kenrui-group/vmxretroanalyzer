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
                resultSetMapping = "TimeCorrelationIdResult")})
@SqlResultSetMapping(
        name = "TimeCorrelationIdResult",
        classes = @ConstructorResult(
                targetClass = com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId.class,
                columns = {
                        @ColumnResult(name = "time"),
                        @ColumnResult(name = "id1"),
                        @ColumnResult(name = "id2")}))
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
