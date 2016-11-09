
import com.erindavide.data.User
import com.erindavide.parser.TelegramUpdateParser
import db.FakeStorage
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

/**
 * Created by linnal on 11/7/16.
 */
class TestTelegramUpdateParser : Spek({
    describe("a calculator") {

        it("should return start message when /help is given as input") {
            val user = User(1, "1", "name")
            val text = "/help"

            val telegramParser = TelegramUpdateParser(FakeStorage())
            val telegramResponse = telegramParser.parseUserMessage(user, text)

            assert(telegramResponse.startsWith("Here you can follow"))
        }

        it("should return start message when /start is given as input") {
            val user = User(1, "1", "name")
            val text = "/start"

            val telegramParser = TelegramUpdateParser(FakeStorage())
            val telegramResponse = telegramParser.parseUserMessage(user, text)

            assert(telegramResponse.startsWith("Here you can follow"))
        }

        it("should return all rss when nothing is given as input") {
            val user = User(1, "1", "name")
            val text = ""
            val storage = FakeStorage()
            storage.addRss(user, "http://localhost.com")

            val telegramParser = TelegramUpdateParser(storage)
            val telegramResponse = telegramParser.parseUserMessage(user, text)

            assertEquals("Rss you are follwing: \n" + "http://localhost.com", telegramResponse)
        }

        it("should add nothing if rss is not correct") {
            val user = User(1, "1", "name")
            val text = "#http://localhost.com"
            val storage = FakeStorage()
            storage.addRss(user, "http://localhost.com")

            val telegramParser = TelegramUpdateParser(storage)
            val telegramResponse = telegramParser.parseUserMessage(user, text)

            assertEquals(1, storage.getAllRss().size)
            assertEquals("Rss URL is not valid", telegramResponse)
        }

        it("should add correct rss") {
            val user = User(1, "1", "name")
            val text = "https://localhost.com"
            val storage = FakeStorage()
            storage.addRss(user, "http://localhost.com")

            val telegramParser = TelegramUpdateParser(storage)
            telegramParser.parseUserMessage(user, text)

            assertEquals(2, storage.getAllRss().size)
        }

        it("should delete nothing if rss is missing") {
            val user = User(1, "1", "name")
            val text = "/delete"
            val storage = FakeStorage()

            val telegramParser = TelegramUpdateParser(storage)
            val telegramResponse = telegramParser.parseUserMessage(user, text)

            assertEquals("Missing url feed you want to delete.", telegramResponse)
        }

    }
})