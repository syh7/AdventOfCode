package syh.year2022.day7

import syh.readSingleLineFile


fun main() {
    val lines = readSingleLineFile("year2022/day7/actual.txt")

    val root = readDirectory(lines)

    val smallDirectories = findDirectoriesSmallerThanSize(root, 100000L)
    println("Found ${smallDirectories.size} small directories:")
    smallDirectories.forEach { println("${it.name} (${it.getSize()})") }
    val totalSize = smallDirectories.sumOf { it.getSize() }
    println("Total size: $totalSize")
}

private fun findDirectoriesSmallerThanSize(root: Directory, maxSize: Long): List<Directory> {
    val dirList = mutableListOf<Directory>()
    for (child in root.children) {
        dirList.addAll(findDirectoriesSmallerThanSize(child, maxSize))
    }
    if (root.getSize() <= maxSize) {
        dirList.add(root)
    }
    return dirList
}
