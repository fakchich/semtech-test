import com.semtech.test.service.DepartmentStatisticService;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        DepartmentStatisticService departmentStatisticService = new DepartmentStatisticService();

        try {
            departmentStatisticService.loadAndProcessFile(args[0]);
        } catch (IOException e) {
            System.out.println("Error while loading input file " + e.getMessage());
        }
    }

}
