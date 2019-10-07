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
                // One tick to multiple quote excluding those that have repeating correlation id group (id1 and id2)
                query = "SELECT TIME, ID1, ID2 " +
                        "FROM CORRELATION " +
                        "WHERE ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) > 1) " +
                        "AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1)",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.checkIfAllOneTickToMultipleQuotesAreCorrelatable",
                query = "SELECT COUNT(*) AS FOUND FROM POINT WHERE ID IN (\n" +
                        "SELECT  ID2\n" +
                        "FROM  CORRELATION\n" +
                        "WHERE  ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) > 1)\n" +
                        "  AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1))",
                resultSetMapping = "NumberFound"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForOneTickToOneQuote",
                // One tick to one quote excluding those that have repeating correlation id group (id1 and id2)
//                query = "SELECT TIME, ID1, ID2 " +
//                        "FROM CORRELATION " +
//                        "WHERE ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) = 1) " +
//                        "AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1)",
                query = "SELECT  TIME, ID1, ID2\n" +
                        "FROM  CORRELATION\n" +
                        "WHERE  ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) = 1)\n" +
                        "  AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID2 HAVING COUNT(*) > 1)\n" +
                        "  AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1)\n" +
                        "  AND ID2 <> '' AND ID2 IS NOT NULL",
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.checkIfAllOneTickToOneQuoteAreCorrelatable",
                query = "SELECT COUNT(*) AS FOUND FROM POINT WHERE ID IN (  \n" +
                        "SELECT ID2\n" +
                        "FROM CORRELATION\n" +
                        "WHERE ID1 IN (SELECT ID1 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID1 HAVING COUNT(ID2) = 1)\n" +
                        "AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1))",
                resultSetMapping = "NumberFound"),
        @NamedNativeQuery(
                name = "Correlation.findCorrelationIdsForDifferentTicksToSameQuote",
                // Different tick to same quote excluding those that have repeating correlation id group (id1 and id2)
                query = "SELECT TIME, ID1, ID2 " +
                        "FROM CORRELATION " +
                        "WHERE ID2 IN (SELECT ID2 FROM CORRELATION WHERE POINT = 'POINT25' GROUP BY ID2 HAVING COUNT(ID1) > 1 AND (ID2 <> '' AND ID2 IS NOT NULL))" +
                        "AND ID2 NOT IN (SELECT ID2 FROM CORRELATION WHERE ID2 <> '' AND ID2 IS NOT NULL GROUP BY ID1, ID2 HAVING COUNT(*) > 1)",
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
                resultSetMapping = "TimeCorrelationIdResult"),
        @NamedNativeQuery(
                name = "Correlation.findRepeatingCorrelationIdGroups",
                query = "SELECT ID1, ID2, COUNT(*) AS REPEATS\n" +
                        "FROM CORRELATION\n" +
                        "WHERE  ID2 <> '' AND ID2 IS NOT NULL\n" +
                        "GROUP BY ID1, ID2\n" +
                        "HAVING COUNT(*) > 1\n" +
                        "ORDER BY REPEATS DESC",
                resultSetMapping = "RepeatingCorrelationIdGroups"),
        @NamedNativeQuery(
                name = "Correlation.findNumberOfRepeatingCorrelationIdGroups",
                query = "SELECT SUM(REPEATS) AS REPEATS\n" +
                        "FROM (SELECT\tID1, ID2, COUNT(*) AS REPEATS\n" +
                        "FROM\tCORRELATION\n" +
                        "WHERE  ID2 <> '' AND ID2 IS NOT NULL\n" +
                        "GROUP BY ID1, ID2\n" +
                        "HAVING\tCOUNT(*) > 1)",
                resultSetMapping = "NumberOfRepeatingCorrelationIdGroups"),
        @NamedNativeQuery(
                name = "Correlation.findNumberOfPointsFoundFromRepeatingCorrelationIdGroups",
                query = "SELECT COUNT(*) AS CORRELATED_VIA_DEDUPLICATED_CORRELATION_ID_GROUPS\n" +
                        "FROM  (SELECT ID, COUNT(*)\n" +
                        "  FROM POINT\n" +
                        "  WHERE ID IN (SELECT  ID2 AS ID\n" +
                        "      FROM  (SELECT ID1, ID2, COUNT(*) AS REPEATS\n" +
                        "        FROM CORRELATION\n" +
                        "        WHERE  ID2 <> '' AND ID2 IS NOT NULL\n" +
                        "        GROUP BY ID1, ID2\n" +
                        "        HAVING COUNT(*) > 1))\n" +
                        "  GROUP BY ID)",
                resultSetMapping = "NumberOfPointsFoundFromRepeatingCorrelationIdGroups")
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
        ),
        @SqlResultSetMapping(
                name = "RepeatingCorrelationIdGroups",
                classes = @ConstructorResult(
                        targetClass = com.kenrui.retroanalyzer.database.compositekeys.RepeatingCorrelationId.class,
                        columns = {
                                @ColumnResult(name = "id1"),
                                @ColumnResult(name = "id2"),
                                @ColumnResult(name = "repeats", type = Integer.class)
                        })
        ),
        @SqlResultSetMapping(
                name = "NumberFound",
                columns = {
                        @ColumnResult(name = "FOUND")
                }
        ),
        @SqlResultSetMapping(
                name = "NumberOfRepeatingCorrelationIdGroups",
                columns = {
                        @ColumnResult(name = "REPEATS")
                }
        ),
        @SqlResultSetMapping(
                name = "NumberOfPointsFoundFromRepeatingCorrelationIdGroups",
                columns = {
                        @ColumnResult(name = "CORRELATED_VIA_DEDUPLICATED_CORRELATION_ID_GROUPS")
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
