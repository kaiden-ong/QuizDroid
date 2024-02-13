package edu.uw.ischool.kong314.quizdroid

class TempTopicRepository : TopicRepository {
    override fun getTopics(): List<Topic> {
        return listOf(
            Topic(
                "Math",
                "Math topics overview",
                "This is an overview of math topics.",
                listOf(
                    Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
                    Question("What is the square root of 16?", listOf("2", "3", "4", "5"), 2),
                    Question("Solve for x: 2x + 5 = 15", listOf("3", "4", "5", "6"), 2)
                )
            ),
            Topic(
                "Physics",
                "Physics topics overview",
                "This is an overview of physics topics.",
                listOf(
                    Question("What is Newton's second law of motion?", listOf("F = ma", "E = mc^2", "a = F/m", "v = at"), 0),
                    Question("What is the formula for kinetic energy?", listOf("1/2 * m * v^2", "mgh", "mv", "mc^2"), 0),
                    Question("What is the unit of electric charge?", listOf("Coulomb", "Ampere", "Volt", "Ohm"), 0)
                )
            ),
            Topic(
                "Marvel Superheroes",
                "Marvel topics overview",
                "This is an overview of Marvel topics.",
                listOf(
                    Question("Who is the alter ego of Spider-Man?", listOf("Bruce Banner", "Tony Stark", "Peter Parker", "Matt Murdock"), 2),
                    Question("What is the real name of Captain America?", listOf("Steve Rogers", "Tony Stark", "Bruce Banner", "Clark Kent"), 0),
                    Question("What is Thor's hammer called?", listOf("Stormbreaker", "Mjolnir", "Gungnir", "Excalibur"), 1)
                )
            )
        )
    }
}