package space.mairi.application_architecture.test;

import java.util.ArrayList;
import java.util.List;

public class TestJava {
    public String  toBeOrNotToBe;

    public String getToBeOrNotToBe(){

        //List<String> strings = new ArrayList<>();
        //List<Object> objects = strings;

        List<String> strings1 = new ArrayList<>();
        strings1.add("asdasdasd");

        List<? extends Object> objects = strings1;
        //objects.add(78);

        Object read = objects.get(0);

        List<CharSequence> chars = new ArrayList<>();
        List<? super String> string = chars;
        string.add("asasasas");

        //String str = string.get(0);

        return toBeOrNotToBe;
    }
}
