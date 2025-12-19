package com.fabrik12.monopolyappwallet.data.models

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class WebSocketSerializationTest {
    private val gson = Gson()

    @Test
    fun `websocket message payload deserializes to GameState`() {
        val json = """
        {
          "type":"GAME_CREATED",
          "payload": {
            "gameId": "partida456",
            "status": "started",
            "players": [
              {"id":"player123","name":"Juan","balance":1500,"properties":["mediterranean_avenue"]}
            ]
          }
        }
        """.trimIndent()

        val wsMsg = gson.fromJson(json, WebSocketMessage::class.java)
        assertNotNull(wsMsg)
        assertEquals("GAME_CREATED", wsMsg.type)
        assertNotNull(wsMsg.payload)

        val gameState = gson.fromJson(wsMsg.payload, GameState::class.java)
        assertNotNull(gameState)
        assertEquals("partida456", gameState.gameId)
        assertEquals("started", gameState.status)
        assertEquals(1, gameState.players.size)

        val player = gameState.players[0]
        assertEquals("player123", player.id)
        assertEquals("Juan", player.name)
        assertEquals(1500, player.balance)
        assertEquals(1, player.properties.size)
        assertEquals("mediterranean_avenue", player.properties[0])
    }
}
