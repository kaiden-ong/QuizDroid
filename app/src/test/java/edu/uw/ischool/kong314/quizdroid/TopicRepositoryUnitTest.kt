package edu.uw.ischool.kong314.quizdroid

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TopicRepositoryUnitTest {
    private val repository = TempTopicRepository()
    private val topics = repository.getTopics()
    @Test
    fun correctNumTopics() {
        assertEquals(3, topics.size)
    }

    fun mathChecks() {
        val mathTopic = topics[0]
        assertEquals("Math", mathTopic.title)
        assertEquals("Math topics overview", mathTopic.shortDescription)
        assertEquals("This is an overview of math topics.", mathTopic.longDescription)
        assertEquals(3, mathTopic.questions.size)
    }

    fun physicsChecks() {
        val physicsTopic = topics[1]
        assertEquals("Physics", physicsTopic.title)
        assertEquals("Physics topics overview", physicsTopic.shortDescription)
        assertEquals("This is an overview of physics topics.", physicsTopic.longDescription)
        assertEquals(3, physicsTopic.questions.size)
    }

    fun marvelChecks() {
        val marvelTopic = topics[2]
        assertEquals("Marvel Superheroes", marvelTopic.title)
        assertEquals("Marvel topics overview", marvelTopic.shortDescription)
        assertEquals("This is an overview of Marvel topics.", marvelTopic.longDescription)
        assertEquals(3, marvelTopic.questions.size)
    }
}