/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import rs.etf.sab.student.KN190588_ArticleOperations.*;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author Neca
 */
public class test {
    public static void main(String[] args) {
        int s=1;
        /*KN190588_ArticleOperations a=new KN190588_ArticleOperations();
        int s=a.createArticle(1,"nov1",1000);*/
        //Kupac kupac=new KN190588_BuyerOperations();
        /*kupac.createBuyer("Minja Krivokapic", 1);
        kupac.createBuyer("Neca Krivokapic", 50);
        kupac.getCity(1);
        kupac.getCity(50);
        kupac.setCity(15, 2);
        kupac.setCity(50, 1);
        kupac.increaseCredit(15, new BigDecimal(10));
        kupac.getCredit(15);*/
        //Shop shop= new KN190588_ShopOperations();
        //shop.createShop("WinWinPo", "Pozarevac");
        //shop.setDiscount(1, 15);
        //shop.setDiscount(100, 15);
        //City city=new KN190588_CityOperations();

        //city.connectCities(1, 2, 10);
        //city.connectCities(3, 1, 10);
        //city.connectCities(4, 2, 30);
        
        //Order order= new KN190588_OrderOperations();
        //Kupac kupac=new KN190588_BuyerOperations();
        //order.addArticle(1, 2, 2);
        //order.getSentTime(1);
        //order.getRecievedTime(1);
        //order.addArticle(1, 1, 5);
        //order.addArticle(1, 1, 7);
        //order.getDiscountSum(1);
        //order.completeOrder(1);
        
        
        /*KN190588_OrderOperations order = new KN190588_OrderOperations();
        order.getDiscountSum(2);
        order.getFinalPrice(2);*/
        /*insertBuyer();
        changeCity();
        credit();
        orders();
        KN190588_GeneralOperations gen=new KN190588_GeneralOperations();
        gen.eraseAll();*/
        KN190588_GeneralOperations gen=new KN190588_GeneralOperations();
        //gen.eraseAll();
        //insertBuyer();
        //changeCity();
        //credit();
        //orders();
        test1();
        KN190588_OrderOperations order=new KN190588_OrderOperations();
        //order.getLocation(1);
    }
    
        public static void test1() {
        KN190588_TransactionOperations trans=new KN190588_TransactionOperations();
        KN190588_CityOperations city=new KN190588_CityOperations();
        KN190588_BuyerOperations kupac=new KN190588_BuyerOperations();
        KN190588_ArticleOperations artikal=new KN190588_ArticleOperations();
        KN190588_GeneralOperations general=new KN190588_GeneralOperations();
        KN190588_OrderOperations order1=new KN190588_OrderOperations();
        general.eraseAll();
        final Calendar initialTime = Calendar.getInstance();
        initialTime.clear();
        initialTime.set(2018, 0, 1);
        general.setInitialTime(initialTime);
        final Calendar receivedTime = Calendar.getInstance();
        receivedTime.clear();
        receivedTime.set(2018, 0, 22);
        final int cityB = city.createCity("B");
        final int cityC1 = city.createCity("C1");
        final int cityA = city.createCity("A");
        final int cityC2 = city.createCity("C2");
        final int cityC3 = city.createCity("C3");
        final int cityC4 = city.createCity("C4");
        final int cityC5 = city.createCity("C5");
        city.connectCities(cityB, cityC1, 8);
        city.connectCities(cityC1, cityA, 10);
        city.connectCities(cityA, cityC2, 3);
        city.connectCities(cityC2, cityC3, 2);
        city.connectCities(cityC3, cityC4, 1);
        city.connectCities(cityC4, cityA, 3);
        city.connectCities(cityA, cityC5, 15);
        city.connectCities(cityC5, cityB, 2);
        KN190588_ShopOperations shop=new KN190588_ShopOperations();
        final int shopA = shop.createShop("shopA", "A");
        final int shopC2 = shop.createShop("shopC2", "C2");
        final int shopC3 = shop.createShop("shopC3", "C3");
        shop.setDiscount(shopA, 20);
        shop.setDiscount(shopC2, 50);
        final int laptop = artikal.createArticle(shopA, "laptop", 1000);
        final int monitor = artikal.createArticle(shopC2, "monitor", 200);
        final int stolica = artikal.createArticle(shopC3, "stolica", 100);
        final int sto = artikal.createArticle(shopC3, "sto", 200);
        shop.increaseArticleCount(laptop, 10);
        shop.increaseArticleCount(monitor, 10);
        shop.increaseArticleCount(stolica, 10);
        shop.increaseArticleCount(sto, 10);
        final int buyer = kupac.createBuyer("kupac", cityB);
        kupac.increaseCredit(buyer, new BigDecimal("20000"));
        final int order = kupac.createOrder(buyer);
        order1.addArticle(order, laptop, 5);
        order1.addArticle(order, monitor, 4);
        order1.addArticle(order, stolica, 10);
        order1.addArticle(order, sto, 4);
        Assert.assertNull(order1.getSentTime(order));
        Assert.assertTrue("created".equals(order1.getState(order)));
        order1.completeOrder(order);
        Assert.assertTrue("sent".equals(order1.getState(order)));
        final int buyerTransactionId = trans.getTransationsForBuyer(buyer).get(0);
        Assert.assertEquals(initialTime, trans.getTimeOfExecution(buyerTransactionId));
        Assert.assertNull(trans.getTransationsForShop(shopA));
        final BigDecimal shopAAmount = new BigDecimal("5").multiply(new BigDecimal("1000")).setScale(3);
        final BigDecimal shopAAmountWithDiscount = new BigDecimal("0.8").multiply(shopAAmount).setScale(3);
        final BigDecimal shopC2Amount = new BigDecimal("4").multiply(new BigDecimal("200")).setScale(3);
        final BigDecimal shopC2AmountWithDiscount = new BigDecimal("0.5").multiply(shopC2Amount).setScale(3);
        final BigDecimal shopC3AmountWithDiscount;
        final BigDecimal shopC3Amount = shopC3AmountWithDiscount = new BigDecimal("10").multiply(new BigDecimal("100")).add(new BigDecimal("4").multiply(new BigDecimal("200"))).setScale(3);
        final BigDecimal amountWithoutDiscounts = shopAAmount.add(shopC2Amount).add(shopC3Amount).setScale(3);
        final BigDecimal amountWithDiscounts = shopAAmountWithDiscount.add(shopC2AmountWithDiscount).add(shopC3AmountWithDiscount).setScale(3);
        final BigDecimal systemProfit = amountWithDiscounts.multiply(new BigDecimal("0.05")).setScale(3);
        final BigDecimal shopAAmountReal = shopAAmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        final BigDecimal shopC2AmountReal = shopC2AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        final BigDecimal shopC3AmountReal = shopC3AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        Assert.assertEquals(amountWithDiscounts, order1.getFinalPrice(order));
        Assert.assertEquals(amountWithoutDiscounts.subtract(amountWithDiscounts), order1.getDiscountSum(order));
        Assert.assertEquals(amountWithDiscounts, trans.getBuyerTransactionsAmmount(buyer));
        Assert.assertEquals(trans.getShopTransactionsAmmount(shopA), new BigDecimal("0").setScale(3));
        Assert.assertEquals(trans.getShopTransactionsAmmount(shopC2), new BigDecimal("0").setScale(3));
        Assert.assertEquals(trans.getShopTransactionsAmmount(shopC3), new BigDecimal("0").setScale(3));
        Assert.assertEquals(new BigDecimal("0").setScale(3), trans.getSystemProfit());
        //System.out.println(initialTime.getTime());
        general.time(2);
        //stem.out.println(initialTime.getTime());

        Assert.assertEquals(initialTime, order1.getSentTime(order));
        Assert.assertNull(order1.getRecievedTime(order));
        Assert.assertEquals(order1.getLocation(order), cityA);
        general.time(9);
        Assert.assertEquals(order1.getLocation(order), cityA);
        general.time(8);
        Assert.assertEquals(order1.getLocation(order), cityC5);
        general.time(5);
        Assert.assertEquals(order1.getLocation(order), cityB);
        Assert.assertEquals(receivedTime, order1.getRecievedTime(order));
        Assert.assertEquals(shopAAmountReal, trans.getShopTransactionsAmmount(shopA));
        Assert.assertEquals(shopC2AmountReal, trans.getShopTransactionsAmmount(shopC2));
        Assert.assertEquals(shopC3AmountReal, trans.getShopTransactionsAmmount(shopC3));
        Assert.assertEquals(systemProfit, trans.getSystemProfit());
        final int shopATransactionId = trans.getTransactionForShopAndOrder(order, shopA);
        Assert.assertNotEquals(-1L, shopATransactionId);
        Assert.assertEquals(receivedTime, trans.getTimeOfExecution(shopATransactionId));
    }
    
    
    
    
    public static void insertBuyer() {
        KN190588_CityOperations city= new KN190588_CityOperations();
        KN190588_BuyerOperations kupac= new KN190588_BuyerOperations();
        final int cityId = city.createCity("Kragujevac");
        Assert.assertNotEquals(-1L, cityId);
        System.out.println((-1!=cityId));
        final int buyerId = kupac.createBuyer("Pera", cityId);
        Assert.assertNotEquals(-1L, buyerId);
        System.out.println((-1!=buyerId));
    }
    
    
    public static void changeCity() {
        KN190588_CityOperations city= new KN190588_CityOperations();
        KN190588_BuyerOperations kupac= new KN190588_BuyerOperations();
        final int cityId1 = city.createCity("Kragujevac");
        final int cityId2 = city.createCity("Beograd");
        final int buyerId = kupac.createBuyer("Lazar", cityId1);
        kupac.setCity(buyerId, cityId2);
        final int cityId3 = kupac.getCity(buyerId);
        Assert.assertEquals(cityId2, cityId3);
        System.out.println((cityId2==cityId2));
       
    }
    
    
    public static void credit() {
        
        KN190588_CityOperations city= new KN190588_CityOperations();
        KN190588_BuyerOperations kupac= new KN190588_BuyerOperations();
        final int cityId = city.createCity("Kragujevac");
        final int buyerId = kupac.createBuyer("Pera", cityId);
        final BigDecimal credit1 = new BigDecimal("1000.000").setScale(3);
        BigDecimal creditReturned = kupac.increaseCredit(buyerId, credit1);
        Assert.assertEquals(credit1, creditReturned);
        System.out.println((credit1.equals(creditReturned)));
        final BigDecimal credit2 = new BigDecimal("500");
        kupac.increaseCredit(buyerId, credit2).setScale(3);
        creditReturned = kupac.getCredit(buyerId);
        BigDecimal b=credit1.add(credit2);
        Assert.assertEquals(credit1.add(credit2), creditReturned);
        System.out.println((b.equals(creditReturned)));
    }
    
    
    public static void orders() {
        KN190588_CityOperations city= new KN190588_CityOperations();
        KN190588_BuyerOperations kupac= new KN190588_BuyerOperations();
        final int cityId = city.createCity("Kragujevac");
        final int buyerId = kupac.createBuyer("Pera", cityId);
        final int orderId1 = kupac.createOrder(buyerId);
        final int orderId2 = kupac.createOrder(buyerId);
        Assert.assertNotEquals(-1L, orderId1);
        Assert.assertNotEquals(-1L, orderId2);
        System.out.println((-1L!=orderId1));
        System.out.println((-1L!=orderId2));
        final List<Integer> orders = kupac.getOrders(buyerId);
        Assert.assertEquals(2L, orders.size());
        System.out.println((2L== orders.size()));
        Assert.assertTrue(orders.contains(orderId1) && orders.contains(orderId2));
        System.out.println(orders.contains(orderId1) && orders.contains(orderId2));
       
       
    }
    
    /*
    public static void general() {
        KN190588_GeneralOperations gen=new KN190588_GeneralOperations();
        final Calendar time = Calendar.getInstance();
        time.clear();
        time.set(2018, 0, 1);
        gen.setInitialTime(time);
        Calendar currentTime = gen.getCurrentTime();
        Assert.assertEquals(time, currentTime);
        gen.time(40);
        currentTime = gen.getCurrentTime();
        final Calendar newTime = Calendar.getInstance();
        newTime.clear();
        newTime.set(2018, 1, 10);
        Assert.assertEquals(currentTime, newTime);
    }
    
    
    
    public static void createShop() {
        KN190588_CityOperations city=new KN190588_CityOperations();
        KN190588_ShopOperations shop=new KN190588_ShopOperations();
        final int cityId = city.createCity("Kragujevac");
        Assert.assertNotEquals(-1L, cityId);
        final int shopId = shop.createShop("Gigatron", "Kragujevac");
        int a= city.getShops(cityId).get(0);
        //Assert.assertEquals(shopId, city.getShops(cityId).get(0));
    }

    public static void setCity() {
        KN190588_CityOperations city=new KN190588_CityOperations();
        KN190588_ShopOperations shop=new KN190588_ShopOperations();
        city.createCity("Kragujevac");
        final int shopId = shop.createShop("Gigatron", "Kragujevac");
        final int cityId2 = city.createCity("Subotica");
        shop.setCity(shopId, "Subotica");
        //Assert.assertEquals(shopId, city.getShops(cityId2).get(0));
    }
    
    
    public static void discount() {
        KN190588_CityOperations city=new KN190588_CityOperations();
        KN190588_ShopOperations shop=new KN190588_ShopOperations();
        city.createCity("Kragujevac");
        final int shopId = shop.createShop("Gigatron", "Kragujevac");
        shop.setDiscount(shopId, 20);
        Assert.assertEquals(20L, shop.getDiscount(shopId));
    }
    
    
    public static void articles() {
        KN190588_CityOperations city=new KN190588_CityOperations();
        KN190588_ShopOperations shop=new KN190588_ShopOperations();
        KN190588_ArticleOperations artikal=new KN190588_ArticleOperations();
        city.createCity("Kragujevac");
        final int shopId = shop.createShop("Gigatron", "Kragujevac");
        final int articleId = artikal.createArticle(shopId, "Olovka", 10);
        Assert.assertNotEquals(-1L, articleId);
        final int articleId2 = artikal.createArticle(shopId, "Gumica", 5);
        Assert.assertNotEquals(-1L, articleId2);
        shop.increaseArticleCount(articleId, 5);
        shop.increaseArticleCount(articleId, 2);
        final int articleCount = shop.getArticleCount(articleId);
        Assert.assertEquals(7L, articleCount);
        final List<Integer> articles = shop.getArticles(shopId);
        Assert.assertEquals(2L, articles.size());
        Assert.assertTrue(articles.contains(articleId) && articles.contains(articleId2));
    }
    
    */
}
