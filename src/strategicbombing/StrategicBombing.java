/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategicbombing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author zhuan
 */
public class StrategicBombing {

    static HashMap<Character, Set<Character>> map = new HashMap();
    static HashMap<Set<Character>, String> routeToOrigin = new HashMap();
    static Set<Character> visitedRoute=new HashSet();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        String road = sc.nextLine();
        while (!road.equals("**")) {
            char start = road.charAt(0);
            char end = road.charAt(1);
            if (!map.containsKey(start)) {
                map.put(start, new HashSet<>());
            }
            Set ends = map.get(start);
            ends.add(end);
            if (!map.containsKey(end)) {
                map.put(end, new HashSet<>());
            }
            ends = map.get(end);
            ends.add(start);

            Set<Character> route = new HashSet();
            route.add(start);
            route.add(end);
            routeToOrigin.put(route, road);
            road = sc.nextLine();
        }
        visitedRoute.add('A');
        ArrayList<ArrayList<Set<Character>>> foundRoutes=findRoutes('A', 'B');
        Set<Set<Character>> criticalRoads = getCriticalRoads(foundRoutes);
        for (Set<Character> r:criticalRoads) {
            System.out.println(routeToOrigin.get(r));
        }
        System.out.printf("There are %d disconneting roads.\r\n", criticalRoads.size());
    }

    private static ArrayList<ArrayList<Set<Character>>> findRoutes(char a, char b) {
        ArrayList<ArrayList<Set<Character>>> foundRoutes=new ArrayList();
        if (!map.containsKey(a)) return foundRoutes;
        Set<Character> ends=map.get(a);
        if (ends==null || ends.isEmpty()) return foundRoutes;
        
        for (char end:ends) {
            if (end==b) {
                ArrayList<Set<Character>> routeList=new ArrayList();
                Set<Character> route=new HashSet();
                route.add(a);
                route.add(b);
                routeList.add(route);
                foundRoutes.add(routeList);
            } else {
                if (!visitedRoute.contains(end)) {
                    visitedRoute.add(end);
                    ArrayList<ArrayList<Set<Character>>> foundSubRoutes=findRoutes(end,b);
                    visitedRoute.remove(end);
                    for (ArrayList<Set<Character>> routeList:foundSubRoutes) {
                        Set<Character> route=new HashSet();
                        route.add(a);
                        route.add(end);
                        routeList.add(0,new HashSet(route));
                    }
                    foundRoutes.addAll(foundSubRoutes);
                }
            }
        }
        
        return foundRoutes;
        
    }

    private static Set<Set<Character>> getCriticalRoads(ArrayList<ArrayList<Set<Character>>> foundRoutes) {
        HashSet<Set<Character>> criticalRoads=null;
        for (ArrayList<Set<Character>> routeList:foundRoutes) {
            if (criticalRoads==null) criticalRoads=new HashSet(routeList);
            else {
                criticalRoads.retainAll(routeList);
            }
        }
        return criticalRoads;
    }
}


