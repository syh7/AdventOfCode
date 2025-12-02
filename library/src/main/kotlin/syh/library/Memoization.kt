package syh.library

import java.util.function.BiFunction
import java.util.function.Function

class Memoization<U, V> {

    private val map = mutableMapOf<U, V>()

    fun addBaseCase(u: U, v: V) {
        map[u] = v
    }

    fun memoize(u: U, default: V, keyTransformer: Function<U, List<U>>, valueReducer: BiFunction<V, V, V>): V {
        if (map.containsKey(u)) {
//            println("found $u in map with value ${map[u]}")
            return map[u]!!
        }

        map[u] = default

        val resultList = keyTransformer.apply(u)
//        println("$u transforms into $resultList")
        resultList.forEach {
            val memoise = memoize(it, default, keyTransformer, valueReducer)
            val combinedValue = valueReducer.apply(map[u]!!, memoise)
//            println("$it has value $combinedValue")
            map[u] = combinedValue
        }

        return map[u]!!
    }

    fun memoizeEarlyExit(u: U, default: V, keyTransformer: Function<U, List<U>>, valueReducer: BiFunction<V, V, V>, exitCondition: Function<V, Boolean>): V {
        if (map.containsKey(u)) {
//            println("found $u in map with value ${map[u]}")
            return map[u]!!
        }

        map[u] = default

        val resultList = keyTransformer.apply(u)
//        println("$u transforms into $resultList")
        resultList.forEach {
            val memoise = memoizeEarlyExit(it, default, keyTransformer, valueReducer, exitCondition)
            val combinedValue = valueReducer.apply(map[u]!!, memoise)
//            println("$it has value $combinedValue")
            map[u] = combinedValue
            if (exitCondition.apply(combinedValue)) {
//                println("early exit for $it")
                return combinedValue
            }
        }

        return map[u]!!
    }

}