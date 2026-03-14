package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_withoutRank_returnsAll() {

        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults())
            .thenReturn(listOf(first, second, third))

        val res = controller.getLeaderboard(null)

        assertEquals(3, res.size)
    }

    @Test
    fun test_getLeaderboard_withRank_returnsNearbyPlayers() {

        val list = listOf(
            GameResult(1, "A", 100, 10.0),
            GameResult(2, "B", 90, 10.0),
            GameResult(3, "C", 80, 10.0),
            GameResult(4, "D", 70, 10.0),
            GameResult(5, "E", 60, 10.0),
            GameResult(6, "F", 50, 10.0),
            GameResult(7, "G", 40, 10.0),
            GameResult(8, "H", 30, 10.0)
        )

        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
    }

    @Test
    fun test_getLeaderboard_invalidRank_throwsException() {

        val list = listOf(
            GameResult(1, "A", 100, 10.0),
            GameResult(2, "B", 90, 10.0)
        )

        whenever(mockedService.getGameResults()).thenReturn(list)

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(10)
        }
    }
}