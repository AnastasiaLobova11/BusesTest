import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class BusesTest4 {

    private Buses bs;

    private String[] names = {"16", "85", "40", "56"};
    private String[][] stops = {{"Stachki231", "Pleven", "Sokol", "Centr"},
            {"Mega", "RS", "Krasnidarskay", "Relv", "Pl1", "SFEDU", "339", "Lev"},
            {"Stachki231", "OblBolnica", "Pleven", "Vokzal", "Centr"},
            {"Krasnidarskay", "Stachki231", "Vavilon", "Tekuceva", "Gorizont"}
    };

    //тест проверяет что при создании нескольних автобусов, нумерация маршрутов для каждого из них начинается с 1
    @Test
    public void twoBuses() throws Exception {
        bs = new Buses();
        assertEquals(1, bs.addBus(names[0], stops[0]));
        Buses bs1 = new Buses();
        assertEquals(1, bs1.addBus(names[0], stops[0]));
    }

    //тест проверяет, что при добавлении 2х одинаковых маршрутов, отличающихся лишь порядком остановок,
    //не будет вызвано исключение
    @Test
    public void twoSimilarStops() throws Exception {
        String[][] s = {{"Stachki231", "Pleven", "Sokol", "Centr"},
                {"Pleven", "Stachki231", "Sokol", "Centr"}
        };
        bs = new Buses();
        assertEquals(1, bs.addBus(names[0], s[0]));
        assertEquals(2, bs.addBus(names[1], s[1]));
    }

    //addBus, пытаемся добавить автобусы. если автобусы имеют одинаковые маршрутки, обрабатываем исключение
    //если позникает при добавлении какая-то иная ошибка, то говорим о том, что произошла непредвиденное исключение
    @Test
    public void addBus() {
        bs = new Buses();
        int result;
        try {
            result = bs.addBus(names[0], stops[0]);
            assertEquals(1, result);
            result = bs.addBus(names[1], stops[1]);
            assertEquals(2, result);
            result = bs.addBus(names[1], stops[2]);
            assertEquals(3, result);
            result = bs.addBus(names[3], stops[0]);
            assertEquals(3, result);

            fail("Multiple add");
        } catch (Exception e) {
            String mess = e.getMessage();
            assertEquals(mess, "Already exists for 1");
        }
        try {
            result = bs.addBus(names[3], stops[3]);
            assertEquals(4, result);
        } catch (Exception e) {
            fail("Wrong exception");
        }
    }

    //SbusesForStop, рассматриваем случай когда есть несколько маршрутов, и некоторые из них проходят через указанную остановку

    @Test
    public void testSbusesForStop() throws Exception {

        String bsL[] = {"16", "40", "56"};

        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        List<String> result = bs.sbusesForStop("Stachki231");

        assertTrue(Arrays.equals(bsL, result.toArray()));


    }

    //SbusesForStop, рассматривааем случай когда есть 1 маршрут, и он проходит через указанную остановку
    @Test
    public void testSbusesForStop1() throws Exception {

        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        List<String> result1 = bs.sbusesForStop("Pl1");
        String bsL1[] = {"85"};
        assertTrue(Arrays.equals(bsL1, result1.toArray()));

    }

    //SbusesForStop, рассматривается ситуация, когда через указанную остановку не проходит ни один маршрут
    @Test
    public void testSbusesForStopException() {
        try {
            String nameStop = "CGB";
            String bsL[] = null;

            bs = new Buses();
            for (int i = 0; i < names.length; ++i) {
                bs.addBus(names[i], stops[i]);
            }

            bs.sbusesForStop(nameStop);

        } catch (Exception e) {
            String mess = e.getMessage();
            assertEquals(mess, "No stop");
        }
    }

    //NbusesForStopException, NbusesForStop рассматривааем случай когда есть несколько маршрутов, и некоторые из них проходят через указанную остановку
    @Test
    public void testNbusesForStop() throws Exception {

        Integer bsL[] = {1, 3, 4};

        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Set<Integer> result = bs.nbusesForStop("Stachki231");

        assertTrue(Arrays.equals(bsL, result.toArray()));


    }

    //NbusesForStopExceptionБ рассматривааем случай когда есть 1 маршрут, и он проходит через указанную остановку
    @Test
    public void testNbusesForStop1() throws Exception {

        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Set<Integer> result1 = bs.nbusesForStop("Pl1");
        Integer bsL1[] = {2};
        assertTrue(Arrays.equals(bsL1, result1.toArray()));

    }

    //NbusesForStopException, рассматривается ситуация, когда через указанную остановку не проходит ни один маршрут
    @Test
    public void testNbusesForStopException() {
        try {
            String nameStop = "CGB";

            bs = new Buses();
            for (int i = 0; i < names.length; ++i) {
                bs.addBus(names[i], stops[i]);
            }

            bs.nbusesForStop(nameStop);
        } catch (Exception e) {
            String mess = e.getMessage();
            assertEquals(mess, "No stop");
        }
    }

    //StopsForBus, проверяет верность составленной map для маршрута 16
    @Test
    public void testStopsForBus16() throws Exception {

        String nameBus = "16";
        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Map<String, Set<Integer>> result = bs.stopsForBus(nameBus);
        List<Set<Integer>> exNum = List.of(Set.of(3), Set.of(3, 4), Set.of());
        String[] exStop = {"Stachki231", "Pleven", "Sokol", "Centr"};

        for (Map.Entry<String, Set<Integer>> e : result.entrySet()) {
            String s = e.getKey();
            assertTrue(Arrays.asList(exStop).contains(s));

            Set<Integer> v = e.getValue();
            assertTrue(exNum.contains(v));
        }
    }

    //StopsForBus,проверяет верность составленной map для маршрута 85
    @Test
    public void testStopsForBus85() throws Exception {

        String nameBus = "85";
        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Map<String, Set<Integer>> result = bs.stopsForBus(nameBus);
        List<Set<Integer>> exNum = List.of(Set.of(), Set.of(4));
        String[] exStop = {"Mega", "RS", "Krasnidarskay", "Relv", "Pl1", "SFEDU", "339", "Lev"};

        for (Map.Entry<String, Set<Integer>> e : result.entrySet()) {
            String s = e.getKey();
            assertTrue(Arrays.asList(exStop).contains(s));

            Set<Integer> v = e.getValue();
            assertTrue(exNum.contains(v));
        }

    }

    //StopsForBus,проверяет верность составленной map для маршрута 40
    @Test
    public void testStopsForBus40() throws Exception {


        String nameBus = "40";
        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Map<String, Set<Integer>> result = bs.stopsForBus(nameBus);
        List<Set<Integer>> exNum = List.of(Set.of(), Set.of(1), Set.of(1, 4));
        String[] exStop = {"Stachki231", "OblBolnica", "Pleven", "Vokzal", "Centr"};

        for (Map.Entry<String, Set<Integer>> e : result.entrySet()) {
            String s = e.getKey();
            assertTrue(Arrays.asList(exStop).contains(s));

            Set<Integer> v = e.getValue();
            assertTrue(exNum.contains(v));
        }

    }

    //StopsForBus,проверяет верность составленной map для маршрута 56

    @Test
    public void testStopsForBus56() throws Exception {


        String nameBus = "56";
        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        Map<String, Set<Integer>> result = bs.stopsForBus(nameBus);
        List<Set<Integer>> exNum = List.of(Set.of(), Set.of(2), Set.of(1, 3));
        String[] exStop = {"Krasnidarskay", "Stachki231", "Vavilon", "Tekuceva", "Gorizont"};

        for (Map.Entry<String, Set<Integer>> e : result.entrySet()) {
            String s = e.getKey();
            assertTrue(Arrays.asList(exStop).contains(s));

            Set<Integer> v = e.getValue();
            assertTrue(exNum.contains(v));
        }
    }

    //StopsForBus,проверяет выбрасываемое исключение с текстом No bus для, когда указанно автоба нет
    @Test
    public void testStopsForBus5() {

        try {
            String nameBus = "5";
            bs = new Buses();
            for (int i = 0; i < names.length; ++i) {
                bs.addBus(names[i], stops[i]);
            }
            bs.stopsForBus(nameBus);

        } catch (Exception e) {

            String mess = e.getMessage();
            assertEquals(mess, "No bus");
        }
    }

    //AllBuses, проверяет выдаваемый список названий всех маршрутов автобусов в алфавитном порядке для цифр
    @Test
    public void testAllBuses() throws Exception {
        bs = new Buses();
        for (int i = 0; i < names.length; ++i) {
            bs.addBus(names[i], stops[i]);
        }
        String[] s = {"16", "40", "56", "85"};
        assertTrue(Arrays.equals(s, bs.allBuses().toArray()));

    }

    //AllBuses, проверяет выдаваемый список названий всех маршрутов автобусов  в алфавитном порядке для смешанных названий
    @Test
    public void testAllBuses1() throws Exception {
        bs = new Buses();
        String[] ss = {"noBus", "bus1", "Bus", "busN"};
        for (int i = 0; i < ss.length; ++i) {
            bs.addBus(ss[i], stops[i]);
        }
        String[] s = {"Bus", "bus1", "busN", "noBus"};
        assertTrue(Arrays.equals(s, bs.allBuses().toArray()));

    }
}