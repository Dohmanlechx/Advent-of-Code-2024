fun main(args: Array<String>) {
    val (rules, updates) = Reader.input("d05", split = "\n\n")
    val pages = mutableMapOf<Int, Page>()

    for (rule in rules.split("\n")) {
        val (a, b) = rule.split("|").map(String::toInt)
        pages[a] = pages.getOrDefault(a, Page(a)).apply { aft.addIfAbsent(b) }
        pages[b] = pages.getOrDefault(b, Page(b)).apply { bef.addIfAbsent(a) }
    }

    val validUpdates = mutableListOf<String>()
    val invalidUpdates = mutableListOf<String>()

    updates.split("\n").forEach { update ->
        update.toPageNumbers().let { numbers ->
            numbers.forEachIndexed { i, p ->
                val bef = numbers.subList(0, i)
                val aft = numbers.subList(i + 1, numbers.size)
                val page = pages[p]!!

                if (bef.any { it in page.aft } || aft.any { it in page.bef }) {
                    invalidUpdates.add(numbers.toSortedPages(pages))
                    return@forEach
                }
            }
        }
        validUpdates.add(update)
    }

    require(validUpdates.sum() == 4774) // Part 01
    require(invalidUpdates.sum() == 6004) // Part 02
}

fun List<Int>.toSortedPages(pages: MutableMap<Int, Page>) =
    map { pages[it]!! }.sorted().map(Page::num).joinToString(",")

fun String.toPageNumbers(): List<Int> =
    split(",").map(String::toInt)

fun List<String>.sum(): Int =
    fold(0) { acc, it ->
        val pages = it.split(",").map(String::toInt)
        acc + pages[pages.count() / 2]
    }

fun MutableList<Int>.addIfAbsent(e: Int) {
    if (!contains(e)) add(e)
}

data class Page(
    val num: Int,
    val bef: MutableList<Int> = mutableListOf(),
    val aft: MutableList<Int> = mutableListOf(),
) : Comparable<Page> {
    override fun compareTo(other: Page) = if (num in other.bef) 0 else -1
}
