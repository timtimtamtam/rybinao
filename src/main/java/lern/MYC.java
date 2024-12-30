package lern;

public class MYC {

    int number = 1;
    char letter = '2';
    boolean condition = true;
    String string = "puk";

    void prn() {
        System.out.println(number + "\n" + string+ "\n" + condition + "\n" + letter);
    }

    void plus1(int anotherNumber) {
        number = number + anotherNumber;
    }

}
