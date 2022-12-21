package space.mairi.application_architecture.test



interface FlyingVehicle {
    val engine: Engine

    val greeting : String
        get() = "Hello from the air"

    fun takeOff()
    fun land()
    fun getHeight() : Double

    fun warnUp(){
        //engine.start()
    }
}