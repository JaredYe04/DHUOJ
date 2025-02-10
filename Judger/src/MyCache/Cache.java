/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cache {
    
    private static final long livingTime=600000;


    private static Map<Integer, Problem> problems = new HashMap();

    public static Problem getOneProblem(Integer problemId) {
        return problems.get(problemId);
    }

    public static void putOneProblem(Problem p) {
        Integer key = p.getProblemId();
        problems.put(key, p);
    }
    
    public static boolean contains(Integer key){
        Set<Integer> keys=problems.keySet();
        return keys.contains(key);
    }
    public static Integer getSize(){
        return Integer.valueOf(problems.size());
    }
}
