package year2022.day7

import readFile


fun main() {
    val lines = readFile("year2022/day7/actual.txt")

    val root = readDirectory(lines)


    val maxSize = 70000000L
    val neededSize = 30000000L
    val currentSize = root.getSize()
    val unusedSize = maxSize - currentSize
    val sizeToRemove = neededSize - unusedSize

    println("maxSize: $maxSize")
    println("neededSize: $neededSize")
    println("currentSize: $currentSize")
    println("unusedSize: $unusedSize")
    println("sizeToRemove: $sizeToRemove")

    val applicableDirectories = findDirectoriesBiggerThanSize(root, sizeToRemove)

    applicableDirectories.forEach { println("${it.name} has size ${it.getSize()}") }

    val smallestDirectory = applicableDirectories.minBy { it.getSize() }
    println("smallest directory is ${smallestDirectory.name} with size ${smallestDirectory.getSize()}")
}


private fun findDirectoriesBiggerThanSize(root: Directory, maxSize: Long): List<Directory> {
    val dirList = mutableListOf<Directory>()
    for (child in root.children) {
        dirList.addAll(findDirectoriesBiggerThanSize(child, maxSize))
    }
    if (root.getSize() >= maxSize) {
        dirList.add(root)
    }
    return dirList
}
