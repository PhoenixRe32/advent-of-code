fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                line.toCharArray()
            }
            .map { charArray ->
                charArray.filter { it.isDigit() }
            }
            .map {
                "${it.first()}${it.last()}".toInt()
//                    .also(Int::println)
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line ->
                val orderedWordDigits = digitMap
                    .map { (word, digit) ->
                        line.indexOf(word) to word
                    }
                    .filter { (index, word) -> index != -1 }
                    .sortedBy { it.first }
                    .map { it.second }
                if (orderedWordDigits.isEmpty())
                    line
                else if (orderedWordDigits.size == 1) {
                    val firstWordDigit: String = orderedWordDigits.first()
                    line.replace(firstWordDigit, digitMap[firstWordDigit].toString(), true)
                } else {
                    val firstWordDigit: String = orderedWordDigits.first()
                    val lastWordDigit: String = orderedWordDigits.last()
                    line
                        .replace(firstWordDigit, digitMap[firstWordDigit].toString(), true)
                        .replace(lastWordDigit, digitMap[lastWordDigit].toString(), true)
                }
            }
            .map { charArray ->
                charArray.filter { it.isDigit() }
            }
            .map {
                "${it.first()}${it.last()}".toInt()
//                    .also(Int::println)
            }
            .sum()
    }

    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1).also { println("Test input part 1 result is: $it") } == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2).also { println("Test input part 2 result is: $it") } == 281)

    val input = readInput("Day01_input")
    println("Actual Answer Part 1")
    println("===========")
    part1(input).println()
    println("Actual Answer Part 2 54980")
    println("===========")
    part2(input).println()
}

private val digitMap = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
    "zero" to 0,
//    "ten" to 10,
//    "eleven" to 11,
//    "twelve" to 12,
//    "thirteen" to 13,
//    "fourteen" to 14,
//    "fifteen" to 15,
//    "sixteen" to 16,
//    "seventeen" to 17,
//    "eighteen" to 18,
//    "nineteen" to 19,
//    "twenty" to 20,
)