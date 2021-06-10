package ru.skillbranch.skillarticles.extensions

//Хорошая реализация в функциональном стиле. Но поиск подстрок в строке
// с помощью меанизмов регулярных выражений может занимать длительное время.
// Ниже приложил свою реализацию, не такую красивую, но возможно более быструю.
// Также, если есть желание, можно почитать про алгоритм Бойера-Мура.
fun String?.indexesOf(
    substr: String,
    ignoreCase: Boolean = true
): List<Int> {
    return if (this != null && substr.isNotEmpty()) {
        substr.toRegex(ignoreCaseOpt(ignoreCase))
            .findAll(this)
            .map { it.range.first }
            .toList()
    } else emptyList()
}

//если функция используется только тут, то есть смысл сделать ее приватной.
private fun ignoreCaseOpt(ignoreCase: Boolean) =
    if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOfV2(needle: String, ignoreCase: Boolean = true): List<Int> {

    val indexes = mutableListOf<Int>()

    if (this.isNullOrEmpty() || needle.isEmpty()) return indexes

    var currentIdx = 0

    while (currentIdx > -1) {
        currentIdx = indexOf(needle, currentIdx, ignoreCase)
        if (currentIdx > -1) {
            indexes.add(currentIdx)
            currentIdx += needle.length
        }
    }

    return indexes
}