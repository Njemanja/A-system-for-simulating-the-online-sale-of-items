package rs.etf.sab.student;

/*import operations.*;
import org.junit.Test;
import student.*;*/
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import rs.etf.sab.operations.*;
import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new KN190588_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new KN190588_BuyerOperations();
        CityOperations cityOperations = new KN190588_CityOperations();
        GeneralOperations generalOperations = new KN190588_GeneralOperations();
        OrderOperations orderOperations = new KN190588_OrderOperations();
        ShopOperations shopOperations = new KN190588_ShopOperations();
        TransactionOperations transactionOperations = new KN190588_TransactionOperations();

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,            
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,transactionOperations
        );

        TestRunner.runTests();
    }
}
