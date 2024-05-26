package com.semtech.test.service;

public class DepartmentData {

    record DepartmentLine(String postalCode, String city, Integer population, String department){
    }

    record DepartmentStatistic(String name, Integer totalPopulation, String cityWithLargestPopulation){
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name);
            stringBuilder.append(";");
            stringBuilder.append(totalPopulation);
            stringBuilder.append(";");
            stringBuilder.append(cityWithLargestPopulation);
            return  stringBuilder.toString();
        }
    }
}
