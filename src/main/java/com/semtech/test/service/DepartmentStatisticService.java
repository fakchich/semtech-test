package com.semtech.test.service;

import com.semtech.test.service.DepartmentData.DepartmentLine;
import com.semtech.test.service.DepartmentData.DepartmentStatistic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepartmentStatisticService {

    public void loadAndProcessFile(String csv) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(csv))) {

            Map<String, DepartmentStatistic> records = handleDepartmentStatistics(lines);

            Optional<DepartmentStatistic> departmentWithSmallestPopulation = findDepartmentWithSmallestPopulation(records);

            displayDepartmentStatistics(records, departmentWithSmallestPopulation);


        }
    }

    private void displayDepartmentStatistics(Map<String, DepartmentStatistic> records, Optional<DepartmentStatistic> departmentWithSmallestPopulation) {
        records.values().forEach((record) -> System.out.println(record.toString()));
        departmentWithSmallestPopulation.ifPresent(departmentStatistic -> System.out.println(departmentStatistic.name()));
    }

    Optional<DepartmentStatistic> findDepartmentWithSmallestPopulation(Map<String, DepartmentStatistic> records) {
        return records.values().stream().min(Comparator.comparingLong(DepartmentStatistic::totalPopulation));
    }

    Map<String, DepartmentStatistic> handleDepartmentStatistics(Stream<String> lines) {
        return lines.parallel().skip(1).filter(line -> !line.contains(";;;"))
            .map(DepartmentStatisticService::mapLineToDepartmentLine)
            .filter(Objects::nonNull)
            .collect(
                Collectors
                    .groupingByConcurrent(DepartmentData.DepartmentLine::postalCode,
                                          Collectors.collectingAndThen(Collectors.toList(), list -> {

                                              Integer populationSum = calculatePopulationSum(list);

                                              String cityWithLargestPopulation = findCityWithLargestPopulation(list);

                                              String departmentName = findDepartmentName(list);

                                              return new DepartmentStatistic(departmentName, populationSum, cityWithLargestPopulation);
                                          })));
    }

    static DepartmentLine mapLineToDepartmentLine(String line) {
        try {
            String[] strings = line.split(";");
            return new DepartmentLine(strings[0], strings[1], Integer.valueOf(strings[2]),
                                      strings.length == 4 ? strings[3] : null);
        } catch (Exception exception) {
            //Ignore the line that is bad formatted.
            return null;
        }
    }

    Integer calculatePopulationSum(List<DepartmentLine> list) {
        return list.stream().mapToInt(DepartmentLine::population).sum();
    }

    String findDepartmentName(List<DepartmentLine> list) {
        Optional<DepartmentLine> department = list.stream().filter(str -> str.department() != null
            && !str.department().isEmpty()).findFirst();

        return department.isEmpty() ? list.get(0).postalCode() : department.get().department();
    }

    String findCityWithLargestPopulation(List<DepartmentLine> list) {
        return String.valueOf(list.stream().max(Comparator.comparingInt(DepartmentLine::population)).get().city());
    }
}
