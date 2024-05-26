package com.semtech.test.service;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DepartmentStatisticServiceIntegTest {

    private DepartmentStatisticService departmentStatisticService = new DepartmentStatisticService();

    @Test
    public void loadAndProcessFile() throws IOException {
        departmentStatisticService.loadAndProcessFile("src/test/resources/population_2019.csv");
        Assertions.assertDoesNotThrow(() -> IOException.class);
    }

    @Test
    public void loadAndProcessFileNotFound() {
        Assertions.assertThrows(IOException.class, () -> departmentStatisticService.loadAndProcessFile("src/test/resources/notFound.csv"));
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        departmentStatisticService.loadAndProcessFile("src/test/resources/empty.csv");
        Assertions.assertDoesNotThrow(() -> IOException.class);
    }
}
