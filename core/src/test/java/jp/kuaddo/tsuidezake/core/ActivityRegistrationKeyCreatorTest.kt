package jp.kuaddo.tsuidezake.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.concurrent.thread

class ActivityRegistrationKeyCreatorTest {
    private lateinit var target: ActivityRegistrationKeyCreator

    @Before
    fun setUp() {
        target = ActivityRegistrationKeyCreator()
    }

    @Test
    fun testGenerateKey() {
        val results = List(10) { mutableListOf<String>() }
        val threads = (0..9).map { threadNum ->
            thread {
                repeat(10000) {
                    results[threadNum] += target.generateKey()
                }
            }
        }
        threads.forEach(Thread::join)

        val keyCount = results.flatten().toSet().size
        assertThat(keyCount).isEqualTo(10 * 10000)
    }
}
