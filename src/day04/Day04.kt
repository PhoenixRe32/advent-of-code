package day04

import println
import readInput
import kotlin.math.pow

private val whiteSpaceRegEx = Regex("""\s+""")

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map(::parseCard)
            .sumOf(Card::value)
    }

    fun part2(input: List<String>): Int {
        val cards = input
            .map(::parseCard)
            .map(::CardCounter)

        cards.forEachIndexed { index: Int, cardCounter: CardCounter ->
            (1..cardCounter.card.givenWinningNumbers.size)
                .map { it + index }
                .dropLastWhile { it > cards.size - 1 }
                .forEach { cards[it].addCopies(cardCounter.instances) }
        }
        return cards.sumOf { it.instances }
    }

    val testInput1 = readInput("day04/test1")
    check(part1(testInput1).also { println("Test input part 1 result is: $it") } == 13)

    val input = readInput("day04/input")
    print("Actual Answer Part 1: ")
    part1(input).println()

    println()
    println("======")
    println()

    val testInput2 = readInput("day04/test2")
    check(part2(testInput2).also { println("Test input part 2 result is: $it") } == 30)

    print("Actual Answer Part 2: ")
    part2(input).println()
}

private fun parseCard(card: String) =
    card.substringAfter(':')
        .split('|')
        .map {
            it.trim()
                .split(whiteSpaceRegEx)
                .map(String::trim)
                .map(String::toInt)
        }
        .let {
            Card(it.first().toSet(), it.last().toSet())
        }

private data class Card(
    val winningNumbers: Set<Int>,
    val givenNumbers: Set<Int>,
) {
    val givenWinningNumbers = givenNumbers.intersect(winningNumbers)
    val value = when (givenWinningNumbers.size) {
        0 -> 0
        else -> 2.0.pow(givenWinningNumbers.size - 1).toInt()
    }
}

private data class CardCounter(
    val card: Card,
    var instances: Int = 1
) {
    fun addCopies(copies: Int) {
        instances += copies
    }
}