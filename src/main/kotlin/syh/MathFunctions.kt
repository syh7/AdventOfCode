package syh

fun calculateLCM(numbers: List<Long>): Long {
    var lcm = numbers[0]
    for (i in 1 until numbers.size) {
        lcm = calculateLCM(lcm, numbers[i])
    }
    return lcm
}

fun calculateLCM(a: Long, b: Long): Long {
    val largest = if (a > b) a else b
    var lcm = largest
    while (true) {
        if (lcm % a == 0L && lcm % b == 0L) {
            break
        }
        lcm += largest
    }
    return lcm
}