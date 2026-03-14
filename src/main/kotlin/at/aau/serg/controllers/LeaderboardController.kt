package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
    ): List<GameResult> {

        val sorted = gameResultService.getGameResults()
            .sortedWith(compareByDescending<GameResult> { it.score }
                .thenBy { it.timeInSeconds })

        if (rank == null) {
            return sorted
        }

        if (rank < 1 || rank > sorted.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rank")
        }

        val index = rank - 1

        val start = maxOf(0, index - 3)
        val end = minOf(sorted.size, index + 4)

        return sorted.subList(start, end)
    }

}