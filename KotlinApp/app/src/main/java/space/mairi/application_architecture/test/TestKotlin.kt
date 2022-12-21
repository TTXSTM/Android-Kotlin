package space.mairi.application_architecture.test

import android.app.Person
import android.util.Log
import space.mairi.application_architecture.model.Weather

// В этом классе находится псевдокод для тренировки

class TestKotlin {
    private val TAG = "${TestKotlin::class.java.simpleName}"
    //var nonNullable : String? = ""

    var name : String = "Name"
    val fullName : String? = ""

    fun checkName(s : String) : String? {
        val anyProducer : Producer<Any> = Producer<String>()

        return ""
    }

    fun test() {
        var nonNullable : String = ""
        val nullable : String? = ""

        if (nonNullable != null){
            val length = nonNullable.length
            //nonNullable = null
        }

        val name : String? = "John"
        val nameLength : Int = name?.length ?:1

        val var1 : Int = 9
        val var2 : CharSequence? = var1 as? CharSequence

        val length = nullable!!.length

        val integer : Int = 1
        val double : Double = integer.toDouble()
    }
    fun testCollection(){
        val phrase: Array<String> = arrayOf("I", "love", "Kotlin")
        val lang = phrase[2]
        phrase[1] = "know"

        val count = phrase.size

        val peopleIem : List<Person> = listOf(Person("Vasiliy", 25), Person("Tatiana", 23))
        peopleIem[0].age = 26

        val people : MutableList<Person> =
            mutableListOf(Person("Vasiliy", 25), Person("Tatiana", 23))

        people.add(Person("Peta", 30))

        //public inline fun <T>.filter(predicate: (T) -> Boolean) : List<T>

        val people1 = people.filter{ it.age <30 }.map(fun(it : Person): Int {
            return it.age
        }).find { it > 24 }

        val people2 = people.any { it.age > 20 }
        val people3 = people.all { it.age < 23 }

        val people4 = people.filter(this::funFilter)

        //////////


        //////////

    }
    fun funFilter(person : Person) : Boolean {

        return person.age < 30

    }

    fun saveWeather(weather : Weather){
        Log.d(TAG, "Weather $weather is \$ good")
        Log.d(TAG, "Weather" + weather +  "good")
    }

    class Person(val name : String, var age : Int)
}