import kotlin.time.measureTime

fun main(args: Array<String>) {
    measureTime {
        val line = Reader.input("d09")[0]
        val ids = mutableListOf<Long?>()
        var id = 0L

        for (i in line.indices) {
            val num = "${line[i]}".toInt()
            if (i % 2 != 0) {
                repeat(num) { ids.add(null) }
                continue
            }
            repeat(num) { ids.add(id) }
            id++
        }

        val p1 = ids.fragmentSlow().checksum()
        val p2 = ids.fragmentFast().checksum()
        require(p1 == 6299243228569)
        require(p2 == 6326952672104)
    }.let { println("d09 completed in $it") }
}

private fun List<Long?>.checksum(): Long =
    foldIndexed(0L) { i, acc, id -> acc + i * (id ?: 0) }

private fun List<Long?>.fragmentSlow(): List<Long?> {
    val ids = this.toMutableList()
    while (ids.contains(null)) {
        val lastNotNull = ids.last { it != null }
        while (ids.last() == null) ids.removeLast()
        val firstNullIndex = ids.indexOfFirst { it == null }
        ids.removeAt(firstNullIndex)
        ids.add(firstNullIndex, lastNotNull)
        ids.removeLast()
    }
    return ids
}

private fun List<Long?>.fragmentFast(): List<Long?> {
    val ids = toMutableList()
    for (id in (maxBy { it ?: 0 } ?: 0) downTo 0) {
        val firstOccurrence = ids.indexOfFirst { it == id }
        val lastOccurrence = ids.indexOfLast { it == id }
        val fileSize = firstOccurrence.let { i -> ids.subList(i, lastOccurrence + 1) }.size
        val freeSpaceIndex = ids.withIndex().indexOfFirst { (i, id) ->
            if (firstOccurrence <= i || id != null) false
            else (i..<i + fileSize).all { ids.getOrElse(it) { 0 } == null }
        }
        if (freeSpaceIndex == -1) continue
        for (i in 0..<fileSize) {
            ids[freeSpaceIndex + i] = id
            ids[firstOccurrence + i] = null
        }
    }
    return ids
}