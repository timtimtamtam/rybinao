package lern.inter;

public class Dog extends Mammals{

    @Override
    protected void makeSound(){
        System.out.println("Wouf wouf");
    }

    @Override
    public void walk() {
        System.out.println("Псин идет");
    }
}
