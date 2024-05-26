package com.semtech.test.service;

import com.semtech.test.service.DepartmentData.DepartmentLine;
import com.semtech.test.service.DepartmentData.DepartmentStatistic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DepartmentStatisticServiceTest {

    private DepartmentStatisticService departmentStatisticService = new DepartmentStatisticService();

    @Test
    void shouldFindCityWithLargestPopulation(){
        String actual = departmentStatisticService.findCityWithLargestPopulation(constructListOfDepartmentLine());
        Assertions.assertEquals("Fleury-sur-Andelle", actual);
    }

    @Test
    void shouldFindDepartmentName(){
        String actual = departmentStatisticService.findDepartmentName(constructListOfDepartmentLine());
        Assertions.assertEquals("EURE", actual);
    }

    @Test
    void shouldCalculatePopulationSum(){
        Integer actual = departmentStatisticService.calculatePopulationSum(constructListOfDepartmentLine());
        Assertions.assertEquals(3318, actual);
    }

    @Test
    void shouldFindDepartmentCodeWhenNameIsNull(){
        String actual = departmentStatisticService.findDepartmentName(Collections.singletonList(new DepartmentLine("27",	"Fleury-sur-Andelle",	1872	,null)));
        Assertions.assertEquals("27", actual);
    }

    @Test
    void testHandleDepartmentStatistics(){
        Map<String, DepartmentStatistic> actual = departmentStatisticService.handleDepartmentStatistics(constructLines());

        Assertions.assertEquals(2, actual.size());

        Assertions.assertTrue(actual.containsKey("27"));
        Assertions.assertTrue(actual.containsKey("28"));
        DepartmentStatistic departmentStatistic27 = actual.get("27");

        Assertions.assertEquals("EURE", departmentStatistic27.name());
        Assertions.assertEquals(3318, departmentStatistic27.totalPopulation());
        Assertions.assertEquals("Fleury-sur-Andelle", departmentStatistic27.cityWithLargestPopulation());

        DepartmentStatistic departmentStatistic28 = actual.get("28");

        Assertions.assertEquals("EURE-ET-LOIR", departmentStatistic28.name());
        Assertions.assertEquals(580, departmentStatistic28.totalPopulation());
        Assertions.assertEquals("Billancelles", departmentStatistic28.cityWithLargestPopulation());

    }

    @Test
    void testMapLineToDepartmentLine(){
        DepartmentLine actual = departmentStatisticService.mapLineToDepartmentLine("28;Billancelles;331;EURE-ET-LOIR");
        Assertions.assertEquals("28", actual.postalCode());
        Assertions.assertEquals("Billancelles", actual.city());
        Assertions.assertEquals("EURE-ET-LOIR", actual.department());
        Assertions.assertEquals(331, actual.population());
    }

    @Test
    void testMapLineToDepartmentLineWithBadFormating(){
        DepartmentLine actual = departmentStatisticService.mapLineToDepartmentLine("28,Billancelles;331;EURE-ET-LOIR");
        Assertions.assertNull(actual);
    }

    @Test
    void testHandleDepartmentStatisticsWithBadFormating(){
        Map<String, DepartmentStatistic> actual = departmentStatisticService.handleDepartmentStatistics(Stream.of("postalCode;City;Population;Department",
                                                                                                                  "27,Flancourt-Crescy-en-Roumois;1446;EURE",
                                                                                                                  "28;Billancelles;331;EURE-ET-LOIR"));

        Assertions.assertEquals(1, actual.size());

    }

    @Test
    void shouldFindDepartmentWithSmallestPopulation(){
        Optional<DepartmentStatistic> optActual = departmentStatisticService.findDepartmentWithSmallestPopulation(departmentStatisticService.handleDepartmentStatistics(constructLines()));
        Assertions.assertTrue(optActual.isPresent());

        Assertions.assertEquals("EURE-ET-LOIR", optActual.get().name());
    }

    private List<DepartmentLine> constructListOfDepartmentLine(){
        List<DepartmentLine> departmentLines = new ArrayList<>();
        departmentLines.add(new DepartmentLine("27",	"Flancourt-Crescy-en-Roumois",	1446,	"EURE"));
        departmentLines.add(new DepartmentLine("27",	"Fleury-sur-Andelle",	1872	,"EURE"));
        return departmentLines;
    }

    private Stream<String> constructLines(){

        return Stream.of(                "postalCode;City;Population;Department",
                                                "27;Flancourt-Crescy-en-Roumois;1446;EURE",
                                                "27;Fleury-sur-Andelle;1872;EURE",
                                                "28;Billancelles;331;EURE-ET-LOIR",
                                                "28;BoncÃ©;249;EURE-ET-LOIR"
        );
    }
}
