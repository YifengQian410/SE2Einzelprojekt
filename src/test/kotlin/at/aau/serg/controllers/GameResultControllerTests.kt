package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock(GameResultService::class.java)
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult() {
        val result = GameResult(1, "player1", 100, 10.0)

        whenever(mockedService.getGameResult(1)).thenReturn(result)

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(result, res)
    }

    @Test
    fun test_getAllGameResults() {
        val list = listOf(
            GameResult(1, "p1", 10, 5.0),
            GameResult(2, "p2", 20, 6.0)
        )

        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(list, res)
    }

    @Test
    fun test_addGameResult() {
        val result = GameResult(1, "player1", 50, 12.0)

        controller.addGameResult(result)

        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_deleteGameResult() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}